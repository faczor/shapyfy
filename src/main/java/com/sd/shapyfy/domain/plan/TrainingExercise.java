package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.model.Exercise;

public record TrainingExercise(
        Exercise exercise,
        int sets,
        int reps,
        Double weight,
        boolean isFinished) {
}
