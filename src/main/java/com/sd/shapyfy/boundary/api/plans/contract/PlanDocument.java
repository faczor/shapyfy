package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;

import java.util.List;
import java.util.UUID;

public record PlanDocument(
        @JsonProperty(value = "id", required = true) UUID id,
        @JsonProperty(value = "training_days", required = true) List<TrainingDayDocument> trainingDays) {

    public static PlanDocument from(PlanConfiguration planConfiguration) {
        return new PlanDocument(
                planConfiguration.plan().id().getValue(),
                planConfiguration.configurationDays().stream().map(TrainingDayDocument::from).toList()
        );
    }
}
