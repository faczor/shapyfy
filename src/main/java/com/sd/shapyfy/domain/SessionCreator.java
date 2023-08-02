package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.SessionService.EditableSessionParams;
import com.sd.shapyfy.domain.plan.ConfigurationDay;
import com.sd.shapyfy.domain.plan.PlanConfiguration;
import com.sd.shapyfy.domain.session.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionCreator {

    private final SessionService sessionService;

    @EventListener(OnTrainingActivationEvent.class)
    public void createFollowUpsOnTrainingActivation(OnTrainingActivationEvent event) {
        createFollowUp(event.getPlanConfiguration(), event.getLastTrainingDate());
    }

    private void createFollowUp(PlanConfiguration planConfiguration, LocalDate lastTrainingDate) {
        int restDaysBetweenTrainings = planConfiguration.restDaysAfterTraining() + planConfiguration.restDaysBeforeTraining();
        LocalDate startDate = lastTrainingDate.plusDays(1).plusDays(restDaysBetweenTrainings);

        createSession(planConfiguration.configurationDays(), startDate);
    }

    private void createSession(List<ConfigurationDay> configurationDays, LocalDate startDate) {
        for (int index = 0; index < configurationDays.size(); index++) {
            ConfigurationDay configurationDay = configurationDays.get(index);
            if (configurationDay.isTrainingDay()) {
                EditableSessionParams editableSessionParams = new EditableSessionParams(
                        SessionState.FOLLOW_UP,
                        startDate.plusDays(index),
                        configurationDay.exercises().stream().map(trainingExercise -> new EditableSessionParams.SessionExerciseExerciseEditableParam(
                                trainingExercise.exercise().getId(),
                                trainingExercise.sets(),
                                trainingExercise.reps(),
                                trainingExercise.weight(),
                                false
                        )).toList()
                );
                sessionService.createSession(configurationDay.id(), editableSessionParams);
            }
        }
    }
}
