package com.sd.shapyfy.domain.plan.model;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class ConfigurationDayId {

    UUID value;

    public static ConfigurationDayId of(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "SessionPartId::" + value;
    }
}