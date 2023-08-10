package com.sd.shapyfy.domain.configuration.model;

import com.sd.shapyfy.domain.plan.model.Plan;
import com.sd.shapyfy.domain.plan.model.SessionId;

import java.util.List;

public record TrainingConfiguration(
        Plan plan,
        SessionId sessionId,
        List<ConfigurationDay> configurationDays) {

    public int daysPlanAmount() {
        return configurationDays().size();
    }

}
