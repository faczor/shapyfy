package com.sd.shapyfy.domain.session.model;

import com.sd.shapyfy.domain.exercises.Exercise;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
public class Session {

    SessionId id;

    SessionType sessionType;

    List<SessionExercise> sessionExercises;

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
