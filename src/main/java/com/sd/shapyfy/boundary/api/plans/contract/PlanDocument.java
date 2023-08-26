package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;

import java.util.List;
import java.util.UUID;

public record PlanDocument(
        @JsonProperty(value = "id", required = true) UUID id,
        @JsonProperty(value = "state", required = true) PlanState state,
        @JsonProperty(value = "training_days", required = true) List<TrainingDayDocument> trainingDays) {

    public static PlanDocument from(TrainingConfiguration trainingConfiguration) {
        return new PlanDocument(
                trainingConfiguration.plan().id().getValue(),
                PlanState.NOT_STARTED, //TODO
                trainingConfiguration.configurationDays().stream().map(TrainingDayDocument::from).toList()
        );
    }
}
