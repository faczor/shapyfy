package com.shapyfy.core.domain.model;

import com.shapyfy.core.domain.TrainingPlanCreator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TrainingPlan(
        TrainingPlanId id,
        String name,
        Status status,
        List<PlanDay> days,
        LocalDate startDate,
        UserId userId) {

    public static TrainingPlan from(TrainingPlanCreator.CreateTrainingPlanRequest createTrainingPlanRequest, UserId userId) {
        return new TrainingPlan(
                TrainingPlanId.newVal(),
                createTrainingPlanRequest.name(),
                Status.INACTIVE,
                createTrainingPlanRequest.requests().stream().map(PlanDay::from).toList(),
                createTrainingPlanRequest.startDate(),
                userId
        );
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public PlanDay nextDay(PlanDay planDay) {
        int indexOfSearchedPlanDay = days.indexOf(planDay);
        if (indexOfSearchedPlanDay == days.size() - 1) {
            return days().getFirst();
        }
        return days.get(days.indexOf(planDay) + 1);
    }

    public PlanDay dayBefore(PlanDay currentDay) {
        int indexOfSearchedPlanDay = days.indexOf(currentDay);
        if (indexOfSearchedPlanDay == 0) {
            return days().getLast();
        }
        return days.get(indexOfSearchedPlanDay - 1);
    }

    private record TrainingPlanId(String value) {

        public static TrainingPlanId newVal() {
            return new TrainingPlanId(UUID.randomUUID().toString());
        }
    }
}
