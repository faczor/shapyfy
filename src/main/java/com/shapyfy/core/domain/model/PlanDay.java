package com.shapyfy.core.domain.model;

import com.shapyfy.core.domain.TrainingPlanCreator;
import com.shapyfy.core.domain.legacy.configuration.model.PlanDayType;

import java.util.List;

public record PlanDay(
        PlayDayId id,
        String name,
        PlanDayType type,
        List<WorkoutExerciseConfig> workoutExerciseConfigConfigurations
) {
    public static PlanDay from(TrainingPlanCreator.CreateConfigurationRequest.CreateConfigurationDayRequest createConfigurationDayRequest) {
        return new PlanDay(
                new PlayDayId("1241241"),
                createConfigurationDayRequest.name(),
                createConfigurationDayRequest.type(),
                createConfigurationDayRequest.createExerciseConfigurationRequests().stream().map(WorkoutExerciseConfig::from).toList());
    }

    private record PlayDayId(String value) {
    }
}
