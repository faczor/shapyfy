package com.sd.shapyfy.boundary.api.trainings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.boundary.api.trainings.contract.CurrentTrainingResponseDocument.DayDocument.TrainingExercise;

import java.util.List;

public record CurrentSessionDocument(

        @JsonProperty(value = "session_id", required = true)
        String sessionId,

        @JsonProperty(value = "training_exercises", required = true)
        List<TrainingExercise> trainingExercises
) {

    public static CurrentSessionDocument from(com.sd.shapyfy.domain.model.Session session) {
        return new CurrentSessionDocument(
                session.getId().getValue().toString(),
                session.getSessionExercises().stream().map(TrainingExercise::from).toList()
        );
    }
}
