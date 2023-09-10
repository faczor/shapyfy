package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreatePlanDocument(
        @NotNull
        @JsonProperty(value = "name") String name,
        //
        @NotNull
        @JsonProperty(value = "exercise_attributes") List<String> exerciseAttributes,
        //
        @NotNull
        @JsonProperty(value = "set_attributes") List<String> setAttributes,
        //
        @NotEmpty
        @NotNull
        @JsonProperty(value = "day_configurations")
        List<DayConfiguration> dayConfigurations) {

    public record DayConfiguration(
            @NotNull
            @JsonProperty(value = "name") String name,
            //
            @NotNull
            @JsonProperty(value = "type") SessionPartType type,
            //
            @JsonProperty(value = "selections") List<SelectedExercise> selectedExercises) {
        public record SelectedExercise(
                @NotEmpty
                @JsonProperty(value = "exercise_id", required = true)
                String exerciseId,
                //
                @NotNull
                @JsonProperty(value = "sets_amount", required = true)
                int sets,
                //
                @NotNull
                @JsonProperty(value = "reps_amount", required = true)
                int reps,
                //
                @JsonProperty(value = "weight_amount")
                Double weight,
                //
                @NotNull
                @JsonProperty(value = "rest_between_sets")
                TimeAmountDocument timeRestBetweenSets) {
        }
    }
}
