package com.shapyfy.core.domain.model;

import com.shapyfy.core.domain.TrainingPlanCreator.CreateTrainingPlanRequest.CreatePlanDayRequest.CreateWorkoutExerciseConfigRequest;

public record WorkoutExerciseConfig(
        WorkoutExerciseConfigId id,
        Exercise exercise,
        int sets,
        int reps,
        double weight,
        int restTime,
        int order) {

    //TODO Add order and rest time
    public static WorkoutExerciseConfig from(CreateWorkoutExerciseConfigRequest request) {
        return new WorkoutExerciseConfig(
                null,
                request.exercise(),
                request.sets(),
                request.reps(),
                request.weight(),
                request.restTime(),
                request.order());
    }

    public record WorkoutExerciseConfigId(String value) {
    }
}
