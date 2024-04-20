package com.shapyfy.core.domain.model;

import com.shapyfy.core.domain.TrainingPlanCreator.CreateTrainingPlanRequest.CreatePlanDayRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.*;

@Entity(name = "plan_days")
@Table(name = "plan_days")
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = PROTECTED, force = true)
@Value
public class PlanDay {

    @EmbeddedId
    PlanDayId id;

    String name;

    PlanDayType type;

    @OneToMany(mappedBy = "planDay", cascade = CascadeType.ALL, orphanRemoval = true)
    List<WorkoutExerciseConfig> workoutExerciseConfigs;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "training_plan_id")
    TrainingPlan plan;

    public static PlanDay from(CreatePlanDayRequest createPlanDayRequest, TrainingPlan plan) {

        PlanDay planDay = new PlanDay(
                PlanDayId.createNew(),
                createPlanDayRequest.name(),
                createPlanDayRequest.type(),
                new ArrayList<>(),
                plan
        );
        if (planDay.isWorkoutDay()) {
            createPlanDayRequest.createWorkoutExerciseConfigs().stream().map(c -> WorkoutExerciseConfig.from(c, planDay)).forEach(planDay.workoutExerciseConfigs::add);
        }

        return planDay;
    }

    private boolean isWorkoutDay() {
        return type == PlanDayType.WORKOUT_DAY;
    }

    @Embeddable
    @AllArgsConstructor(access = PUBLIC, staticName = "of")
    @NoArgsConstructor(access = PROTECTED, force = true)
    @Value
    public static class PlanDayId {
        UUID id;

        public static PlanDayId createNew() {
            return new PlanDayId(UUID.randomUUID());
        }
    }
}
