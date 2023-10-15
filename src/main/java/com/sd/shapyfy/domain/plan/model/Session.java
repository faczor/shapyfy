package com.sd.shapyfy.domain.plan.model;

import com.google.common.collect.Iterables;
import com.sd.shapyfy.domain.DateRange;
import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record Session(

        SessionId sessionId,
        SessionState state,
        List<SessionPart> sessionParts) {

    public DateRange dateRange() {
        return new DateRange(
                sessionParts().get(0).date(),
                Iterables.getLast(sessionParts()).date()
        );
    }

    public LocalDate lastDate() {
        return dateRange().end();
    }

    public boolean isActive() {
        return state.isActive();
    }

    //TODO proper exception
    public SessionPart partFor(LocalDate localDate) {
        return sessionParts.stream().filter(part -> part.date().equals(localDate)).findFirst().orElseThrow();
    }

    public SessionPart partById(SessionPartId sessionPartId) {
        return sessionParts.stream().filter(part -> Objects.equals(part.id(), sessionPartId)).findFirst()
                .orElseThrow();
    }

    public List<SessionPart> partsWithExercise(ExerciseId exerciseId) {
        return sessionParts().stream().filter(part -> part.isContainingExercise(exerciseId)).toList();
    }

    public boolean areExercisesFinished() {
        return sessionParts().stream().allMatch(SessionPart::isFinished);
    }
}
