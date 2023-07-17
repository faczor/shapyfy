package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.TrainingPort.FollowUpTrainingSession;
import com.sd.shapyfy.domain.model.Training;
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
        log.info("Creating follow up sessions for training: {}", event.getTraining().getId());
        Training training = event.getTraining();
        LocalDate lastTrainingDate = event.getLastTrainingDate();

        LocalDate followUpStartDate = lastTrainingDate
                .plusDays(1)
                .plusDays(training.restDaysAfterTraining());

        List<FollowUpTrainingSession> dayDates = followUpSessionFor(training, followUpStartDate);

        trainingPort.createFollowUpSessions(dayDates);
    }

    public List<TrainingPort.ActivateSession> createForActivation(Training training, TrainingDayId trainingDayId, LocalDate startDate) {
        List<TrainingDay> trainingDays = selectTrainingDaysToActivate(training, trainingDayId);

        List<TrainingPort.ActivateSession> activateSessions = new ArrayList<>();
        for (TrainingDay trainingDay : trainingDays) {
            if (trainingDay.isTrainingDay()) {
                activateSessions.add(new TrainingPort.ActivateSession(
                        trainingDay.draftSession().getId(),
                        startDate)
                );
            }

            startDate = startDate.plusDays(1);
        }

        return activateSessions;
    }

    private List<TrainingDay> selectTrainingDaysToActivate(Training training, TrainingDayId trainingDayId) {
        List<TrainingDay> trainingDays = new ArrayList<>();
        int trainingDayIndex = getTrainingDayIndex(training, trainingDayId);
        for (int dayIndex = trainingDayIndex; dayIndex < training.getTrainingDays().size(); dayIndex++) {
            trainingDays.add(training.getTrainingDays().get(dayIndex));
        }
        return trainingDays;
    }

    private int getTrainingDayIndex(Training training, TrainingDayId trainingDayId) {
        for (int dayIndex = 0; dayIndex < training.getTrainingDays().size(); dayIndex++) {
            if (training.getTrainingDays().get(dayIndex).getId().equals(trainingDayId)) {
                return dayIndex;
            }
        }

        throw new IllegalStateException();
    }

    private static List<FollowUpTrainingSession> followUpSessionFor(Training training, LocalDate startDate) {
        List<FollowUpTrainingSession> trainingDayDates = new ArrayList<>();
        for (TrainingDay trainingDay : training.getTrainingDays()) {
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
