package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.configuration.model.Attribute;
import com.sd.shapyfy.domain.configuration.model.ExerciseSet;
import com.sd.shapyfy.domain.configuration.model.TrainingExerciseId;
import com.sd.shapyfy.domain.exercise.model.Exercise;

import java.util.List;

public record TrainingExercise(
        TrainingExerciseId id,
        Exercise exercise,
        int restBetweenSets,
        boolean isFinished,
        List<Attribute> attributes,
        List<ExerciseSet> sets) {
}
