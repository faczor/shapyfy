package com.sd.shapyfy.boundary.api.dashboard.contact;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record UserDashboardContract(
        @JsonProperty(value = "user_meta_data", required = true)
        MetaDataDocument metaDataDocument,
        @JsonProperty(value = "day_states", required = true)
        List<DayState> dayStates) {
    public record DayState(
            @JsonProperty(value = "id", required = false)
            String id,
            @JsonProperty(value = "training_id", required = true)
            String trainingDay,
            @JsonProperty(value = "date", required = true)
            LocalDate date,
            @JsonProperty(value = "state", required = true)
            DayType dayType) {
        public enum DayType {
            TRAINING_DAY,
            REST_DAY,
            NO_TRAINING
        }
    }

    public record MetaDataDocument(
            TrainingState state) {
    }
}
