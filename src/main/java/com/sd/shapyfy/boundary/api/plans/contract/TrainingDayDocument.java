package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.model.TrainingDay;
import com.sd.shapyfy.domain.model.ConfigurationDayType;
import com.sd.shapyfy.domain.plan.ConfigurationDay;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public record TrainingDayDocument(
        @JsonProperty(value = "id", required = true) UUID dayId,
        @JsonProperty(value = "name") String name,
        @JsonProperty(value = "day_of_week", required = true) DayOfWeek day,
        @JsonProperty(value = "type", required = true) ConfigurationDayType type,
        @JsonProperty(value = "exercises", required = true) List<ExerciseDocument> exerciseDocuments) {


    //TODO exercises???
    public static TrainingDayDocument from(ConfigurationDay configurationDay) {
        return new TrainingDayDocument(
                configurationDay.id().getValue(),
                configurationDay.name(),
                //configurationDay.getDay(),
                null,
                configurationDay.type(),
                List.of()
        );
    }
}
