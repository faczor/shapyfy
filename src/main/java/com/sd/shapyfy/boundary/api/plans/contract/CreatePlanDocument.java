package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayType;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;

public record CreatePlanDocument(
        @JsonProperty(value = "name") String name,

        @NotEmpty
        @NotNull
        @JsonProperty(value = "day_configurations")
        List<DayConfiguration> dayConfigurations) {

    public record DayConfiguration(
            @JsonProperty(value = "name") String name,
            @NotNull
            @JsonProperty(value = "type") SessionPartType type,

            @JsonProperty(value = "selections") List<SelectedExercise> selectedExercises) {
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
}
