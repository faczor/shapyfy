package com.sd.shapyfy.boundary.api.sessions.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AttributeFillValueDocument(
        @JsonProperty(value = "id")
        UUID id,
        @JsonProperty(value = "value")
        String value) {
}
