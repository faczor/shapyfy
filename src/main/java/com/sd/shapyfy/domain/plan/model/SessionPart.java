package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.domain.configuration.model.TrainingExercise;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;

import java.time.LocalDate;
import java.util.List;

public record SessionPart(

        SessionPartId sessionPartId,
        ConfigurationDayId configurationDayId,
        SessionState state,
        LocalDate date,
        List<TrainingExercise> trainingExercises) {
}
