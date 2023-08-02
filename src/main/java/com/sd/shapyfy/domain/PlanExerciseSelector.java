package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.ExerciseId;
import com.sd.shapyfy.domain.model.UserId;
import com.sd.shapyfy.domain.plan.ConfigurationDay;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanExerciseSelector {

    private final ConfigurationService configurationService;

    public ConfigurationDay select(ConfigurationDayId configurationDayId, List<SelectedExercise> selectedExercises, UserId userId) {
        log.info("Attempt to select exercises {} for configuration day {} for {}", selectedExercises, configurationDayId, userId);
        return configurationService.fillConfigurationWithExercises(configurationDayId, selectedExercises);
    }

    public record SelectedExercise(
            ExerciseId exerciseId,
            int sets,
            int reps,
            Double weight) {
    }

}
