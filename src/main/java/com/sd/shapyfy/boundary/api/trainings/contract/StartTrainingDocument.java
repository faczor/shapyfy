package com.sd.shapyfy.boundary.api.trainings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record StartTrainingDocument(
        @JsonProperty(value = "start_date") LocalDate startDate,
        @JsonProperty(value = "initialize_training_with_day_id") String trainingDayStartId) {
}
