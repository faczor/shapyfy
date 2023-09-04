package com.sd.shapyfy.domain.configuration.model;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class AttributeId {

    UUID id;

    @Override
    public String toString() {
        return "AttributeId::" + id;
    }
}
