package com.sd.shapyfy.domain.configuration.exception;

import com.sd.shapyfy.domain.InvalidDomainResourceState;

public class ConfigurationWithoutTrainingDays extends InvalidDomainResourceState {
    public ConfigurationWithoutTrainingDays(String message) {
        super(message);
    }
}
