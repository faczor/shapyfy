package com.sd.shapyfy.domain.configuration;

import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanExerciseSelector {

    private final ConfigurationService configurationService;

    public ConfigurationDay select(PlanId planId, ConfigurationDayId configurationDayId, List<SelectedExercise> selectedExercises, UserId userId) {
        log.info("Attempt to select exercises {} for configuration day {} for {}", selectedExercises, configurationDayId, userId);
        return configurationService.updateOrCreateFutureSessionConfiguration(
                new ConfigurationService.EditTargetQuery(
                        planId,
                        configurationDayId,
                        SessionState.DRAFT),
                new ConfigurationService.EditParams(
                        null,
                        SessionState.DRAFT,
                        null,
                        selectedExercises.stream().map(s -> new ConfigurationService.EditParams.SessionExerciseExerciseEditableParam(
                                s.exerciseId(), s.sets(), s.reps(), s.weight(), false)).toList())
        );
    }

    public record SelectedExercise(
            ExerciseId exerciseId,
            int sets,
            int reps,
            Double weight) {
    }

}
