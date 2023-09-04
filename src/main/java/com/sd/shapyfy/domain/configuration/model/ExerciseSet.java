package com.sd.shapyfy.domain.configuration.model;

import java.util.List;

public record ExerciseSet(
        ExerciseSetId id,
        int reps,
        Double weight,
        boolean isFinished,
        List<Attribute> attributes) {
}
