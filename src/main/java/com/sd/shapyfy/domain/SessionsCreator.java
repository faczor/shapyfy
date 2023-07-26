package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.TrainingPort.FollowUpTrainingSession;
import com.sd.shapyfy.domain.model.Plan;
import com.sd.shapyfy.domain.model.TrainingDay;
import com.sd.shapyfy.domain.model.TrainingDayId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionsCreator {

    private final TrainingPort trainingPort;

    @EventListener(StartedTrainingEvent.class)
    public void createFollowUpSessions(StartedTrainingEvent event) {
        log.info("Creating follow up sessions for training: {}", event.getPlan().getId());
        Plan plan = event.getPlan();
        LocalDate lastTrainingDate = event.getLastTrainingDate();

        LocalDate followUpStartDate = lastTrainingDate
                .plusDays(1)
                .plusDays(plan.restDaysAfterTraining());

        List<FollowUpTrainingSession> dayDates = followUpSessionFor(plan, followUpStartDate);

        trainingPort.createFollowUpSessions(dayDates);
    }

    public List<TrainingPort.ActivateSession> createForActivation(Plan plan, TrainingDayId trainingDayId, LocalDate startDate) {
        List<TrainingDay> trainingDays = selectTrainingDaysToActivate(plan, trainingDayId);

        List<TrainingPort.ActivateSession> activateSessions = new ArrayList<>();
        for (TrainingDay trainingDay : trainingDays) {
            if (trainingDay.isTrainingDay()) {
                activateSessions.add(new TrainingPort.ActivateSession(
                        trainingDay.draftSession().id(),
                        startDate)
                );
            }

            startDate = startDate.plusDays(1);
        }

        return activateSessions;
    }

    private List<TrainingDay> selectTrainingDaysToActivate(Plan plan, TrainingDayId trainingDayId) {
        List<TrainingDay> trainingDays = new ArrayList<>();
        int trainingDayIndex = getTrainingDayIndex(plan, trainingDayId);
        for (int dayIndex = trainingDayIndex; dayIndex < plan.getTrainingDays().size(); dayIndex++) {
            trainingDays.add(plan.getTrainingDays().get(dayIndex));
        }
        return trainingDays;
    }

    private int getTrainingDayIndex(Plan plan, TrainingDayId trainingDayId) {
        for (int dayIndex = 0; dayIndex < plan.getTrainingDays().size(); dayIndex++) {
            if (plan.getTrainingDays().get(dayIndex).getId().equals(trainingDayId)) {
                return dayIndex;
            }
        }

        throw new IllegalStateException();
    }

    private static List<FollowUpTrainingSession> followUpSessionFor(Plan plan, LocalDate startDate) {
        List<FollowUpTrainingSession> trainingDayDates = new ArrayList<>();
        for (TrainingDay trainingDay : plan.getTrainingDays()) {
            if (trainingDay.isTrainingDay()) {
                trainingDayDates.add(new FollowUpTrainingSession(
                        trainingDay.getId(),
                        startDate)
                );
            }

            startDate = startDate.plusDays(1);
        }

        return trainingDayDates;
    }
}
