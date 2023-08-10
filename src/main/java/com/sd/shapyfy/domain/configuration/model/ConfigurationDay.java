package com.sd.shapyfy.domain.configuration.model;


import com.sd.shapyfy.domain.plan.model.SessionPartId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.util.List;

public record ConfigurationDay(
        SessionPartId id,
        SessionPartType type,
        String name,
        List<TrainingExercise> exercises) {

    public boolean isTrainingDay() {
        return type == SessionPartType.TRAINING_DAY;
    }

}
