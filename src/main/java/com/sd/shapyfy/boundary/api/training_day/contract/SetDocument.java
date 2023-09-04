package com.sd.shapyfy.boundary.api.training_day.contract;

import com.sd.shapyfy.domain.configuration.model.ExerciseSet;

import java.util.List;

public record SetDocument(

        String id,
        int reps,
        Double weight,
        boolean isFinished,
        List<AttributeDocument> attributes) {
    public static SetDocument from(ExerciseSet exerciseSet) {
        return new SetDocument(
                exerciseSet.id().getValue().toString(),
                exerciseSet.reps(),
                exerciseSet.weight(),
                exerciseSet.isFinished(),
                exerciseSet.attributes().stream().map(AttributeDocument::from).toList()
        );
    }
}
