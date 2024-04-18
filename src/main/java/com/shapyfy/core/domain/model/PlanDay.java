package com.shapyfy.core.domain.model;

import com.shapyfy.core.domain.TrainingPlanCreator;

import java.util.List;

public record PlanDay(
        PlanDayId id,
        String name,
        PlanDayType type,
        List<WorkoutExerciseConfig> workoutExerciseConfigConfigurations
) {
    public static PlanDay from(TrainingPlanCreator.CreateTrainingPlanRequest.CreatePlanDayRequest createPlanDayRequest) {
        return new PlanDay(
                null,
                createPlanDayRequest.name(),
                createPlanDayRequest.type(),
                createPlanDayRequest.requests().stream().map(WorkoutExerciseConfig::from).toList());
    }

    public record PlanDayId(String value) {
    }
}
