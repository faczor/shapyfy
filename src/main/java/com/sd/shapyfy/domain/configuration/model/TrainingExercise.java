package com.sd.shapyfy.domain.configuration.model;

import com.sd.shapyfy.domain.exercise.model.Exercise;

//TODO decide if part of configuration or separate thing
public record TrainingExercise(
        Exercise exercise,
        int sets,
        int reps,
        Double weight,
        boolean isFinished) {
}
