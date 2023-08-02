package com.sd.shapyfy.domain.exercise.model;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class ExerciseId {

    UUID value;

    public static ExerciseId of(String value) {
        return ExerciseId.of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "ExerciseId::" + value;
    }
}
