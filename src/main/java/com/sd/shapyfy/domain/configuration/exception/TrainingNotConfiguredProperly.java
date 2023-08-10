package com.sd.shapyfy.domain.configuration.exception;

import com.sd.shapyfy.domain.InvalidDomainResourceState;
import com.sd.shapyfy.domain.plan.model.SessionPartId;

import java.util.List;

public class TrainingNotConfiguredProperly extends InvalidDomainResourceState {
    public TrainingNotConfiguredProperly(List<SessionPartId> sessionPartIds) {
        super("Training days not configured properly " + sessionPartIds);
    }
}
