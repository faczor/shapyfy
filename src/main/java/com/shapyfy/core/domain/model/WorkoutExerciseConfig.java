package com.shapyfy.core.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.shapyfy.core.domain.TrainingPlanCreator.CreateTrainingPlanRequest.CreatePlanDayRequest.CreateWorkoutExerciseConfigRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity(name = "workout_exercise_configs")
@Table(name = "workout_exercise_configs")
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = PROTECTED, force = true)
@Value
public class WorkoutExerciseConfig {

    @EmbeddedId
    WorkoutExerciseConfigId id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    Exercise exercise;

    int sets;

    int reps;

    double weight;

    @Column(name = "rest_time")
    int restTime;

    @OrderBy
    @Column(name = "order_index")
    int order;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "plan_day_id")
    PlanDay planDay;

    public static WorkoutExerciseConfig from(CreateWorkoutExerciseConfigRequest request, PlanDay planDay) {
        return new WorkoutExerciseConfig(
                WorkoutExerciseConfigId.createNew(),
                request.exercise(),
                request.sets(),
                request.reps(),
                request.weight(),
                request.restTime(),
                request.order(),
                planDay
        );
    }

    @Embeddable
    @AllArgsConstructor(access = PUBLIC, staticName = "of")
    @NoArgsConstructor(access = PROTECTED, force = true)
    @Value
    public static class WorkoutExerciseConfigId {

        UUID id;

        public static WorkoutExerciseConfigId createNew() {
            return new WorkoutExerciseConfigId(UUID.randomUUID());
        }
    }
}
