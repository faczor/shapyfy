package com.sd.shapyfy.domain.configuration.model;

import com.sd.shapyfy.domain.plan.model.Plan;
import com.sd.shapyfy.domain.plan.model.SessionId;

import java.util.List;

public record TrainingConfiguration(
        Plan plan,
        SessionId sessionId, //SessionId === ConfigurationId :)
        List<ConfigurationDay> configurationDays) {

    public int daysPlanAmount() {
        return configurationDays().size();
    }

}
