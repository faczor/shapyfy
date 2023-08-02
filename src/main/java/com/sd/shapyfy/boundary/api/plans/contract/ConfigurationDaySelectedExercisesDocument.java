package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ConfigurationDaySelectedExercisesDocument(
        @NotNull
        @NotEmpty
        @JsonProperty(value = "exercises") List<SelectedExercise> selectedExercises) {


    public record SelectedExercise(
            @NotEmpty
            @JsonProperty(value = "exercise_id", required = true)
            String exerciseId,
            //
            @JsonProperty(value = "sets_amount", required = true)
            int sets,
            //
            @JsonProperty(value = "reps_amount", required = true)
            int reps,
            //
            @JsonProperty(value = "weight_amount")
            Double weight,
            //
            @JsonProperty(value = "rest_between_sets_second")
            int secondRestBetweenSets
    ) {
    }
}
