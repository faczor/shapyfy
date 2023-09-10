package com.sd.shapyfy.boundary.api.exercises;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record CreateExerciseDocument(
        @NotNull
        @JsonProperty(value = "name")
        String name) {
}
