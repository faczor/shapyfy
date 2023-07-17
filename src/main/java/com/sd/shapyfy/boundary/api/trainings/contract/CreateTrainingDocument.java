package com.sd.shapyfy.boundary.api.trainings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.model.TrainingDayType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;

public record CreateTrainingDocument(
        @JsonProperty(value = "name") String name,

        @NotEmpty
        @NotNull
        @JsonProperty(value = "day_configurations")
        List<DayConfiguration> dayConfigurations) {

    public record DayConfiguration(
            @JsonProperty(value = "name") String name,
            @NotNull
            @JsonProperty(value = "type") TrainingDayType type,
            @NotNull
            @JsonProperty(value = "day_of_week") DayOfWeek dayOfWeek) {
    }
}
