package com.sd.shapyfy.domain.configuration.model;

import com.sd.shapyfy.domain.plan.model.Plan;
import com.sd.shapyfy.domain.plan.model.SessionPart;

import java.util.List;
import java.util.Objects;

public record TrainingConfiguration(
        Plan plan,
        ConfigurationId configurationId, //SessionId === ConfigurationId :) Change as soon as possible!
        List<ConfigurationAttribute> configurationAttributes,
        List<ConfigurationDay> configurationDays) {

    public int daysPlanAmount() {
        return configurationDays().size();
    }

    //TODO better handling
    public ConfigurationDay forSessionPart(SessionPart sessionPart) {
        return configurationDays.stream().filter(day -> Objects.equals(day.id(), (sessionPart.configurationDayId()))).findFirst().orElseThrow();
    }

}
