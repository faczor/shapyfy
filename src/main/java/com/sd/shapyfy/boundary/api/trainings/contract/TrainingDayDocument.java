package com.sd.shapyfy.boundary.api.trainings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.trainingDay.TrainingDayType;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public record TrainingDayDocument(
        @JsonProperty(value = "id", required = true) UUID dayId,
        @JsonProperty(value = "name") String name,
        @JsonProperty(value = "day_of_week", required = true) DayOfWeek day,
        @JsonProperty(value = "type", required = true) TrainingDayType type,
        @JsonProperty(value = "exercises", required = true) List<ExerciseDocument> exerciseDocuments) {


    public static TrainingDayDocument from(Training.TrainingDay trainingDay) {
        return new TrainingDayDocument(
                trainingDay.getId().getValue(),
                trainingDay.getName(),
                trainingDay.getDay(),
                trainingDay.getDayType(),
                List.of()
        );
    }
}
