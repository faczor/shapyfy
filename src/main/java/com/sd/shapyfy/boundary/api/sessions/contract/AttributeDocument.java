package com.sd.shapyfy.boundary.api.sessions.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.configuration.model.Attribute;

import java.util.UUID;

public record AttributeDocument(
        @JsonProperty(value = "id")
        UUID id,
        @JsonProperty(value = "name")
        String name,
        @JsonProperty(value = "value")
        String value) {
    public static AttributeDocument from(Attribute attribute) {
        return new AttributeDocument(
                attribute.attributeId().getValue(),
                attribute.name(),
                attribute.value()
        );
    }
}
