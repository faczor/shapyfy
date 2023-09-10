package com.sd.shapyfy.domain.configuration.model;

import com.sd.shapyfy.domain.exercise.model.Exercise;

public record ConfiguredExercises(
        TrainingExerciseId id,
        Exercise exercise,
        int sets,
        int reps,
        int breakBetweenSets,
        Double weight) {
}
