package com.shapyfy.core.boundary.api.exercise.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shapyfy.core.domain.model.Exercise;

import java.util.UUID;

public record ExerciseContract(
        //
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name
) {
    public static ExerciseContract from(Exercise exercise) {
        return new ExerciseContract(exercise.getId().getId(), exercise.getName());
    }
}
