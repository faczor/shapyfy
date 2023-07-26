package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.boundary.api.plans.contract.CurrentPlanResponseDocument.DayDocument.TrainingExercise;
import com.sd.shapyfy.domain.session.Session;

import java.util.List;

public record CurrentSessionDocument(

        @JsonProperty(value = "session_id", required = true)
        String sessionId,

        @JsonProperty(value = "training_exercises", required = true)
        List<TrainingExercise> trainingExercises
) {

    public static CurrentSessionDocument from(Session session) {
        return new CurrentSessionDocument(
                session.id().getValue().toString(),
                session.sessionExercises().stream().map(TrainingExercise::from).toList()
        );
    }
}
