package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.configuration.ConfigurationService.EditParams;
import com.sd.shapyfy.domain.configuration.event.OnTrainingActivationEvent;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
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
public class SessionCreator {

    private final ConfigurationService configurationService;

    @EventListener(OnTrainingActivationEvent.class)
    public void createFollowUpsOnTrainingActivation(OnTrainingActivationEvent event) {
        createFollowUp(event.getPlanConfiguration(), event.getLastTrainingDate());
    }

    private void createFollowUp(PlanConfiguration planConfiguration, LocalDate lastTrainingDate) {
        int restDaysBetweenTrainings = planConfiguration.restDaysAfterTraining() + planConfiguration.restDaysBeforeTraining();
        LocalDate startDate = lastTrainingDate.plusDays(1).plusDays(restDaysBetweenTrainings);

        createSession(planConfiguration.plan().id(), planConfiguration.configurationDays(), startDate);
    }

    private void createSession(PlanId planId, List<ConfigurationDay> configurationDays, LocalDate startDate) {
        List<EditParams> createSessionParams = new ArrayList<>();
        for (int index = 0; index < configurationDays.size(); index++) {
            ConfigurationDay configurationDay = configurationDays.get(index);
            if (configurationDay.isTrainingDay()) {
                EditParams editParams = new EditParams(
                        configurationDay.id(),
                        SessionState.FOLLOW_UP,
                        startDate.plusDays(index),
                        configurationDay.exercises().stream().map(trainingExercise -> new EditParams.SessionExerciseExerciseEditableParam(
                                trainingExercise.exercise().id(),
                                trainingExercise.sets(),
                                trainingExercise.reps(),
                                trainingExercise.weight(),
                                false
                        )).toList()
                );
                createSessionParams.add(editParams);
            }
        }
        configurationService.createSession(planId, createSessionParams);
    }
}
