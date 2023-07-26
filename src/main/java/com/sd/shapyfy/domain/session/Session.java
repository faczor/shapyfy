package com.sd.shapyfy.domain.session;

import com.sd.shapyfy.domain.model.Exercise;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

public record Session(

        SessionId id,
        SessionState sessionState,
        LocalDate date,
        List<SessionExercise> sessionExercises) {

    public boolean isActive() {
        return sessionState.isActive();
    }

    public boolean isDraft() {
        return sessionState.isDraft();
    }

    public boolean isRunning() {
        return sessionState.isRunning();
    }

    public List<SessionExercise> notFinishedExercises() {
        return sessionExercises.stream()
                .filter(not(SessionExercise::isFinished))
                .toList();
    }

    public record SessionExercise(

            SessionExerciseId sessionExerciseId,
            int sets,
            int reps,
            Double weight,
            boolean isFinished,

            Exercise exercise) {

        public Optional<Double> getWeight() {
            return Optional.ofNullable(weight);
        }
    }
}
