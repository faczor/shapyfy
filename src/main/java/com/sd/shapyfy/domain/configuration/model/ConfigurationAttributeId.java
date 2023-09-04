package com.sd.shapyfy.domain.configuration.model;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class ConfigurationAttributeId {

    UUID value;

    @Override
    public String toString() {
        return "ConfigurationAttributeId::" + value;
    }
}
