package com.shapyfy.core.boundary.api.training.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shapyfy.core.domain.model.PlanDayType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ConfigurePlanRequest(
        //
        @JsonProperty("name") String name,
        @JsonProperty("start_date") LocalDate startDate,
        @JsonProperty("day_configurations") List<ConfigureDay> configureDays) {
    public record ConfigureDay(

            @JsonProperty("type") PlanDayType type,
            @JsonProperty("name") String name,
            @JsonProperty("exercise_configurations") List<ConfigureExerciseWorkout> requests) {
        public record ConfigureExerciseWorkout(
                //
                @JsonProperty("id") UUID id,
                @JsonProperty("weight") double weight,
                @JsonProperty("sets") int sets,
                @JsonProperty("reps") int reps,
                @JsonProperty("rest_time") int restTime) {
        }
    }
}

