package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.configuration.model.TrainingExercise;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.time.LocalDate;
import java.util.List;

public record SessionPart(

        SessionPartId sessionPartId,
        SessionId configurationDayId,
        SessionPartType state,
        LocalDate date,
        List<TrainingExercise> trainingExercises) {
}
