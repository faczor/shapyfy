package com.shapyfy.core.domain.model;

import com.shapyfy.core.domain.TrainingPlanCreator;

import java.util.List;
import java.util.UUID;

public record TrainingPlan(
        TrainingPlanId id,
        String name,
        List<PlanDay> days,
        UserId userId) {

    public static TrainingPlan from(TrainingPlanCreator.CreateConfigurationRequest createConfigurationRequest, UserId userId) {
        return new TrainingPlan(
                TrainingPlanId.newVal(),
                createConfigurationRequest.name(),
                createConfigurationRequest.createConfigurationDayRequests().stream().map(PlanDay::from).toList(),
                userId
        );
    }

    private record TrainingPlanId(String value) {

        public static TrainingPlanId newVal() {
            return new TrainingPlanId(UUID.randomUUID().toString());
        }
    }
}
