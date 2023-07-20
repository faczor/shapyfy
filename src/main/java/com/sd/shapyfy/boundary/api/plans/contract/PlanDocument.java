package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.model.Training;

import java.util.List;
import java.util.UUID;

public record PlanDocument(
        @JsonProperty(value = "id", required = true) UUID id,
        @JsonProperty(value = "training_days", required = true) List<TrainingDayDocument> trainingDays) {

    public static PlanDocument from(Training training) {
        return new PlanDocument(
                training.getId().getValue(),
                training.getTrainingDays().stream().map(TrainingDayDocument::from).toList()
        );
    }
}
