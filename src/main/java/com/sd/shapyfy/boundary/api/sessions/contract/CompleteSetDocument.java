package com.sd.shapyfy.boundary.api.sessions.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record CompleteSetDocument(

        @JsonProperty(value = "id")
        UUID id,
        @JsonProperty(value = "reps")
        int reps,
        @JsonProperty(value = "weight")
        Double weight,
        @JsonProperty(value = "attributes")
        List<AttributeFillValueDocument> attributes) {
}
