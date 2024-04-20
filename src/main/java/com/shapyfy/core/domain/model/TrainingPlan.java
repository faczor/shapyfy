package com.shapyfy.core.domain.model;

import com.shapyfy.core.domain.TrainingPlanCreator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity(name = "trainingPlans")
@Table(name = "training_plans")
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Value
public class TrainingPlan {

    @EmbeddedId
    TrainingPlanId id;

    String name;

    Status status;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PlanDay> days;

    @Column(name = "start_date")
    LocalDate startDate;

    @Embedded
    UserId userId;

    public static TrainingPlan from(TrainingPlanCreator.CreateTrainingPlanRequest createTrainingPlanRequest, UserId userId) {
        TrainingPlan trainingPlan = new TrainingPlan(
                TrainingPlanId.createNew(),
                createTrainingPlanRequest.name(),
                Status.ACTIVE,
                new ArrayList<>(),
                createTrainingPlanRequest.startDate(),
                userId
        );
        createTrainingPlanRequest.createPlan().stream().map(r -> PlanDay.from(r, trainingPlan)).forEach(trainingPlan.days::add);

        return trainingPlan;
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public PlanDay nextDay(PlanDay planDay) {
        assert days != null;

        int indexOfSearchedPlanDay = days.indexOf(planDay);
        if (indexOfSearchedPlanDay == days.size() - 1) {
            return getDays().getFirst();
        }
        return days.get(days.indexOf(planDay) + 1);
    }

    public PlanDay dayBefore(PlanDay currentDay) {
        assert days != null;

        int indexOfSearchedPlanDay = days.indexOf(currentDay);
        if (indexOfSearchedPlanDay == 0) {
            return getDays().getLast();
        }
        return days.get(indexOfSearchedPlanDay - 1);
    }

    @Embeddable
    @AllArgsConstructor(access = PUBLIC, staticName = "of")
    @NoArgsConstructor(access = PROTECTED, force = true)
    @Value
    public static class TrainingPlanId {

        UUID id;

        public static TrainingPlanId createNew() {
            return new TrainingPlanId(UUID.randomUUID());
        }
    }
}
