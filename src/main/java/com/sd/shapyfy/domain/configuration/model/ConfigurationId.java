package com.sd.shapyfy.domain.configuration.model;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class ConfigurationId {

    UUID value;

    public static ConfigurationId of(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "ConfigurationId::" + value;
    }
}
