package com.shapyfy.core.domain.model;

public record WorkoutSet(WorkoutSetId id, int reps, double weight, Exercise exercise, int order) {
    public record WorkoutSetId(String value) {
    }
}
