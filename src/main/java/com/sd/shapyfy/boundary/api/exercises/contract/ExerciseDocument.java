package com.sd.shapyfy.boundary.api.exercises.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.exercise.model.Exercise;

import java.util.UUID;

public record ExerciseDocument(
        @JsonProperty(value = "id", required = true) UUID id,
        @JsonProperty(value = "name", required = true) String name) {
    //
    public static ExerciseDocument from(Exercise exercise) {
        return new ExerciseDocument(
                exercise.getId().getValue(),
                exercise.getName()
        );
    }
}
