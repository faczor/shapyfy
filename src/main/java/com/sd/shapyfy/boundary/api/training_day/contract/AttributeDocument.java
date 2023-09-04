package com.sd.shapyfy.boundary.api.training_day.contract;

import com.sd.shapyfy.domain.configuration.model.Attribute;

public record AttributeDocument(
        String id,
        String name,
        String value) {
    public static AttributeDocument from(Attribute attribute) {
        return new AttributeDocument(
                attribute.attributeId().getId().toString(),
                attribute.name(),
                attribute.value()
        );
    }
}
