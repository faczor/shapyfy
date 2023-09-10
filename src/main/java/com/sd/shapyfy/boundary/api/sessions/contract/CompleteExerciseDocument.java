package com.sd.shapyfy.boundary.api.sessions.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CompleteExerciseDocument(
        @JsonProperty(value = "attributes")
        List<AttributeFillValueDocument> attributes,

        @JsonProperty(value = "sets")
        List<CompleteSetDocument> sets) {
}
