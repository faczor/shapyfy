package com.sd.shapyfy.boundary.api.plans.contract;

import com.sd.shapyfy.domain.configuration.model.ConfigurationAttribute;

public record AttributeConfigurationDocument(
        String id,
        String name) {
    public static AttributeConfigurationDocument from(ConfigurationAttribute configurationAttribute) {
        return new AttributeConfigurationDocument(
                configurationAttribute.id().getValue().toString(),
                configurationAttribute.name()
        );
    }
}
