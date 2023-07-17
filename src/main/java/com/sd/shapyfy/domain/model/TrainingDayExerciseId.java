package com.sd.shapyfy.domain.model;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class TrainingDayExerciseId {

    UUID value;

    public static TrainingDayExerciseId of(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "TrainingDayExerciseId::" + value;
    }
}
