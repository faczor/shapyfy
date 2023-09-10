package com.sd.shapyfy.boundary.api.sessions.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.util.List;
import java.util.UUID;

public record SessionPartDocument(
        @JsonProperty(value = "id")
        UUID id,
        @JsonProperty(value = "name")
        String name,
        @JsonProperty(value = "type")
        SessionPartType type,
        @JsonProperty(value = "state")
        SessionPartState state,
        @JsonProperty(value = "exercises")
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
