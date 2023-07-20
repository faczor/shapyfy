package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record StartPlanDocument(
        @JsonProperty(value = "start_date") LocalDate startDate,
        @JsonProperty(value = "initialize_training_with_day_id") String trainingDayStartId) {
}
