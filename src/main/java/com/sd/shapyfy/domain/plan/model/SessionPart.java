package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.time.LocalDate;
import java.util.List;

public record SessionPart(
        SessionPartId id,
        String name,
        SessionPartType type,
        SessionPartState state,
        LocalDate date,
        List<TrainingExercise> exercises) {

    public boolean isOngoing() {
        return !(isFinished() && isNotStarted());
    }

    public boolean isFinished() {
        return exercises().stream().allMatch(TrainingExercise::isFinished);
    }

    public boolean isNotStarted() {
        return exercises().stream().noneMatch(TrainingExercise::isFinished);
    }
}
