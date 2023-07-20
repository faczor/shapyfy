package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExerciseAttributesDocument(

        @JsonProperty(value = "weight", required = false)
        Double weight) {
}
