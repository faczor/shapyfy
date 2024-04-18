package com.shapyfy.core.domain.model;

import java.util.UUID;

public record WorkoutSet(WorkoutSetId id, int reps, double weight, Exercise exercise, int order) {

    public static WorkoutSet from(int reps, double weight, Exercise exercise, int index) {
        return new WorkoutSet(WorkoutSetId.newVal(), reps, weight, exercise, index);
    }

    public record WorkoutSetId(String value) {
        public static WorkoutSetId newVal() {
            return new WorkoutSetId(UUID.randomUUID().toString());
        }
    }
}
