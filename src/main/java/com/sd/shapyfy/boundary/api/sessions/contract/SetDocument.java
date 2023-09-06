package com.sd.shapyfy.boundary.api.sessions.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.configuration.model.ExerciseSet;

import java.util.List;
import java.util.UUID;

public record SetDocument(
        @JsonProperty(value = "id")
        UUID id,
        @JsonProperty(value = "reps")
        int reps,
        @JsonProperty(value = "weight")
        Double weight,
        @JsonProperty(value = "is_finished")
        boolean isFinished,
        @JsonProperty(value = "attributes")
        List<AttributeDocument> attributes) {
    public static SetDocument from(ExerciseSet exerciseSet) {
        return new SetDocument(
                exerciseSet.id().getValue(),
                exerciseSet.reps(),
                exerciseSet.weight(),
                exerciseSet.isFinished(),
                exerciseSet.attributes().stream().map(AttributeDocument::from).toList()
        );
    }
}
