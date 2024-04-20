package com.shapyfy.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity(name = "workout_sets")
@Table(name = "workout_sets")
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = PROTECTED, force = true)
@Value
public class WorkoutSet {

    @EmbeddedId
    WorkoutSetId id;

    int reps;

    double weight;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    Exercise exercise;

    @Column(name = "order_index")
    int order;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "activity_log_id")
    ActivityLog activityLog;

    public static WorkoutSet from(int reps, double weight, Exercise exercise, int index, ActivityLog activityLog) {
        return new WorkoutSet(WorkoutSetId.createNew(), reps, weight, exercise, index, activityLog);
    }

    @Value
    @Embeddable
    @AllArgsConstructor(access = PUBLIC, staticName = "of")
    @NoArgsConstructor(access = PROTECTED, force = true)
    public static class WorkoutSetId {

        UUID id;

        public static WorkoutSetId createNew() {
            return new WorkoutSetId(UUID.randomUUID());
        }
    }
}
