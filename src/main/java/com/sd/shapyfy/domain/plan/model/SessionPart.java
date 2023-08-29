package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.configuration.model.TrainingExercise;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.time.LocalDate;
import java.util.List;

public record SessionPart(

        SessionPartId sessionPartId,
        SessionPartId configurationDayId,
        SessionPartType state,
        LocalDate date,
        List<TrainingExercise> trainingExercises) {

    public boolean isOngoing() {
        return !(isFinished() && isNotStarted());
    }

    public boolean isFinished() {
        return trainingExercises().stream().allMatch(TrainingExercise::isFinished);
    }

    public boolean isNotStarted() {
        return trainingExercises().stream().noneMatch(TrainingExercise::isFinished);
    }
}
