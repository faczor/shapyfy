package com.sd.shapyfy.boundary.api.trainings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.trainingDay.TrainingDayType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Value
public class CreateTrainingDocument {

    @JsonProperty(value = "name")
    String name;

    @NotEmpty
    @NotNull
    @JsonProperty(value = "day_configurations")
    List<DayConfiguration> dayConfigurations;


    @Value
    public static class DayConfiguration {

        @JsonProperty(value = "name")
        String name;

        @NotNull
        @JsonProperty(value = "type")
        TrainingDayType type;

        @NotNull
        @JsonProperty(value = "day_of_week")
        DayOfWeek dayOfWeek;

    }
}
