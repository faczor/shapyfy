package com.sd.shapyfy.domain.configuration.model;


import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.util.List;

public record ConfigurationDay(
        ConfigurationDayId id, //TODO ConfigPartId ConfigurationDayId
        SessionPartType type, //TODO rename type enum
        String name,
        List<ConfiguredExercises> exercises) {

    public boolean isTrainingDay() {
        return type == SessionPartType.TRAINING_DAY;
    }

}
