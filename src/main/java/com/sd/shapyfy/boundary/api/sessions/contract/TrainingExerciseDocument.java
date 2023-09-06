package com.sd.shapyfy.boundary.api.sessions.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.boundary.api.exercises.contract.ExerciseDocument;
import com.sd.shapyfy.boundary.api.plans.contract.TimeAmountDocument;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;

import java.util.List;
import java.util.UUID;

public record TrainingExerciseDocument(
        @JsonProperty(value = "id")
        UUID id,
        @JsonProperty(value = "exercise")
        ExerciseDocument exercise,
        @JsonProperty(value = "break_between_sets")
        TimeAmountDocument breakBetweenSets,
        @JsonProperty(value = "is_finished")
        boolean isFinished,
        @JsonProperty(value = "attributes")
        List<AttributeDocument> attributes,
        @JsonProperty(value = "sets")
        List<SetDocument> sets) {

    public static TrainingExerciseDocument from(TrainingExercise trainingExercise) {
        return new TrainingExerciseDocument(
                trainingExercise.id().getValue(),
                ExerciseDocument.from(trainingExercise.exercise()),
                TimeAmountDocument.fromSeconds(trainingExercise.restBetweenSets()),
                trainingExercise.isFinished(),
                trainingExercise.attributes().stream().map(AttributeDocument::from).toList(),
                trainingExercise.sets().stream().map(SetDocument::from).toList()
        );
    }

}
