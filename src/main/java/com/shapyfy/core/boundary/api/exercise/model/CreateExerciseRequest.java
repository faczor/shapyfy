package com.shapyfy.core.boundary.api.exercise.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateExerciseRequest(
        //
        @JsonProperty("name") String name
) {

}
