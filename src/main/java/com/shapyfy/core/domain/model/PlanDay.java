package com.shapyfy.core.domain.model;

import com.shapyfy.core.domain.TrainingPlanCreator;

import java.util.List;

public record PlanDay(
        PlayDayId id,
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

    public record PlayDayId(String value) {
    }
}
