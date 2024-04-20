package com.shapyfy.core.boundary.api.trainingDay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shapyfy.core.domain.model.PlanDayType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PlanDayContract(
        //
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name,
        @JsonProperty("type") PlanDayType type,
        @JsonProperty("exercises") List<WorkoutExerciseContract> exercises
) {
    public record WorkoutExerciseContract(
            //
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name,
            @JsonProperty("config") ExerciseConfigContract config,
            @JsonProperty("history") List<PreviousWorkouts> previousWorkouts
    ) {
        public record ExerciseConfigContract(
                //
                @JsonProperty("reps") int reps,
                @JsonProperty("weight") double weight,
                @JsonProperty("sets") int sets,
                @JsonProperty("restTime") int restTime) {
        }

        public record PreviousWorkouts(
                //
                @JsonProperty("date") LocalDate date,
                @JsonProperty("sets") List<WorkoutSetContract> sets
        ) {
            public record WorkoutSetContract(
                    //
                    @JsonProperty("reps") int reps,
                    @JsonProperty("weight") double weight) {
            }
        }
    }
}
