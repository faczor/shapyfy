package com.sd.shapyfy.domain.configuration.exception;

import com.sd.shapyfy.domain.InvalidDomainResourceState;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;

import java.util.List;

public class TrainingNotConfiguredProperly extends InvalidDomainResourceState {
    public TrainingNotConfiguredProperly(List<ConfigurationDayId> configurationDayIds) {
        super("Training days not configured properly " + configurationDayIds);
    }
}
