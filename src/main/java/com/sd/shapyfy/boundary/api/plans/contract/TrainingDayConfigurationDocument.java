package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.ConfiguredExercises;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record TrainingDayConfigurationDocument(
        @JsonProperty(value = "id", required = true) UUID dayId,
        @JsonProperty(value = "name") String name,
        @JsonProperty(value = "type", required = true) SessionPartType type,
        @JsonProperty(value = "exercises", required = true) List<TrainingDayExerciseDocument> exerciseDocuments) {


    public static TrainingDayConfigurationDocument from(ConfigurationDay configurationDay) {
        return new TrainingDayConfigurationDocument(
                configurationDay.id().getValue(),
                configurationDay.name(),
                configurationDay.type(),
                configurationDay.exercises().stream().map(TrainingDayExerciseDocument::from).toList()
        );
    }

    record TrainingDayExerciseDocument(
            @NotEmpty
            @JsonProperty(value = "exercise_id", required = true)
            UUID exerciseId,
            //
            @NotEmpty
            @JsonProperty(value = "exercise_name", required = true)
            String exerciseName,
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
            @JsonProperty(value = "rest_between_sets")
            TimeAmountDocument timeAmountDocument
    ) {
        public static TrainingDayExerciseDocument from(ConfiguredExercises configuredExercises) {
            return new TrainingDayExerciseDocument(
                    configuredExercises.exercise().id().getValue(),
                    configuredExercises.exercise().name(),
                    configuredExercises.sets(),
                    configuredExercises.reps(),
                    configuredExercises.weight(),
                    TimeAmountDocument.fromSeconds(configuredExercises.breakBetweenSets())
            );
        }
    }
}
