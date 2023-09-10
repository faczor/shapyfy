package com.sd.shapyfy.domain.configuration.model;

import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationAttributeType;

public record ConfigurationAttribute(
        ConfigurationAttributeId id,
        String name,
        ConfigurationAttributeType type) {
    public boolean isForSet() {
        return type == ConfigurationAttributeType.SET;
    }

    public boolean isForExercise() {
        return type == ConfigurationAttributeType.EXERCISE;
    }
}
