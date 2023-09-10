package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;

import java.util.UUID;

public record PlanDocument(
        @JsonProperty(value = "id", required = true) UUID id,
        @JsonProperty(value = "name", required = true) String name,
        @JsonProperty(value = "state", required = true) PlanState state,
        @JsonProperty(value = "configuration", required = true) PlanConfigurationDocument configurationDocument) {

    public static PlanDocument from(TrainingConfiguration trainingConfiguration) {
        return new PlanDocument(
                trainingConfiguration.plan().id().getValue(),
                trainingConfiguration.plan().name(),
                trainingConfiguration.plan().state(),
                PlanConfigurationDocument.from(trainingConfiguration)
        );
    }
}
