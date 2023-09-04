package com.sd.shapyfy.boundary.api.training_day.contract;

import com.sd.shapyfy.boundary.api.exercises.contract.ExerciseDocument;
import com.sd.shapyfy.boundary.api.plans.contract.TimeAmountDocument;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;

import java.util.List;
import java.util.UUID;

public record TrainingExerciseDocument(
        UUID id,
        ExerciseDocument exercise,
        TimeAmountDocument breakBetweenSets,
        boolean isFinished,
        List<AttributeDocument> attributes,
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
