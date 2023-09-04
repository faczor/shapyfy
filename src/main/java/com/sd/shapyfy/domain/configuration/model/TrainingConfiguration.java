package com.sd.shapyfy.domain.configuration.model;

import com.sd.shapyfy.domain.plan.model.Plan;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.SessionPart;

import java.util.List;

public record TrainingConfiguration(
        Plan plan,
        SessionId sessionId, //SessionId === ConfigurationId :) Change as soon as possible!
        List<ConfigurationAttribute> configurationAttributes,
        List<ConfigurationDay> configurationDays) {

    public int daysPlanAmount() {
        return configurationDays().size();
    }

    //TODO better handling
    public ConfigurationDay forSessionPart(SessionPart sessionPart) {
        return configurationDays.stream().filter(day -> day.name().equals(sessionPart.name())).findFirst().orElseThrow();
    }

}
