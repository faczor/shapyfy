package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.time.LocalDate;
import java.util.List;

public record UpdateSessionPartData(
        SessionPartType type,
        LocalDate date,
        String name,
        List<UpdateExercise> updateExercises) {

    public record UpdateExercise(
            ExerciseEntity exercise,
            Integer setsAmount,
            Integer repsAmount,
            Double weightAmount,
            Integer restBetweenSets,
            Boolean isFinished) {
    }
}