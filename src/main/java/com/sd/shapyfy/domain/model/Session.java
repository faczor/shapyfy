package com.sd.shapyfy.domain.model;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Value
public class Session {

    SessionId id;

    SessionState sessionState;

    LocalDate date;

    List<SessionExercise> sessionExercises;

    public boolean isActive() {
        return sessionState == SessionState.ACTIVE;
    }

    public boolean isDraft() {
        return sessionState == SessionState.DRAFT;
    }

    @Value
    public static class SessionExercise {

        SessionExerciseId sessionExerciseId;

        int sets;

        int reps;

        Double weight;

        Exercise exercise;

        public Optional<Double> getWeight() {
            return Optional.ofNullable(weight);
        }
    }
}
