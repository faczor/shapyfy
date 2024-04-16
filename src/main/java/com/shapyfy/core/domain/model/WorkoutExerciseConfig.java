package com.shapyfy.core.domain.model;

import com.shapyfy.core.domain.TrainingPlanCreator;

public record WorkoutExerciseConfig(
        WorkoutExerciseConfigId id,
        Exercise exercise,
        int sets,
        int reps,
        double weight,
        int restTime,
        int order) {

    //TODO Add order and rest time
    public static WorkoutExerciseConfig from(TrainingPlanCreator.CreateConfigurationRequest.CreateConfigurationDayRequest.CreateExerciseConfigurationRequest createExerciseConfigurationRequest) {
        return new WorkoutExerciseConfig(
                new WorkoutExerciseConfigId("1241241"),
                null,
                createExerciseConfigurationRequest.sets(),
                createExerciseConfigurationRequest.reps(),
                createExerciseConfigurationRequest.weight(),
                0,
                0);
    }

    public record WorkoutExerciseConfigId(String value) {
    }
}
