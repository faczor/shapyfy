package com.sd.shapyfy.boundary.api.trainings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.model.Training;

import java.util.List;
import java.util.UUID;

public record TrainingDocument(
        @JsonProperty(value = "id", required = true) UUID id,
        @JsonProperty(value = "training_days", required = true) List<TrainingDayDocument> trainingDays) {

    public static TrainingDocument from(Training training) {
        return new TrainingDocument(
                training.getId().getValue(),
                training.getTrainingDays().stream().map(TrainingDayDocument::from).toList()
        );
    }
}
