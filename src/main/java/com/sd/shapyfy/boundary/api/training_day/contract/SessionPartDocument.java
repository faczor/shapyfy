package com.sd.shapyfy.boundary.api.training_day.contract;

import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.util.List;
import java.util.UUID;

public record SessionPartDocument(
        UUID id,
        String name,
        SessionPartType type,
        SessionPartState state,
        List<TrainingExerciseDocument> exercises) {
    public static SessionPartDocument from(SessionPart sessionPart) {
        return new SessionPartDocument(
                sessionPart.id().getValue(),
                sessionPart.name(),
                sessionPart.type(),
                sessionPart.state(),
                sessionPart.exercises().stream().map(TrainingExerciseDocument::from).toList()
        );
    }
}
