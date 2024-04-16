package com.shapyfy.core.domain.model;

public record WorkoutSet(WorkoutSetId id, int reps, double weight) {
    public record WorkoutSetId(String value) {
    }
}
