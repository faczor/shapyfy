package com.sd.shapyfy.boundary.api.trainings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

record ExerciseDocument(
        @JsonProperty(value = "id", required = true) UUID exerciseId,
        @JsonProperty(value = "name", required = true) String name) {

}
