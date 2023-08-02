package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.TrainingExercise;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record ConfigurationDayDocument(
        UUID id,
        String name,
        List<TrainingDayExerciseDocument> trainingDayExercises
) {
    public static ConfigurationDayDocument from(ConfigurationDay configurationDay) {
        return new ConfigurationDayDocument(
                configurationDay.id().getValue(),
                configurationDay.name(),
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
            @JsonProperty(value = "rest_between_sets_second")
            int secondRestBetweenSets
    ) {
        public static TrainingDayExerciseDocument from(TrainingExercise trainingExercise) {
            return new TrainingDayExerciseDocument(
                    trainingExercise.exercise().id().getValue(),
                    trainingExercise.exercise().name(),
                    trainingExercise.sets(),
                    trainingExercise.reps(),
                    trainingExercise.weight(),
                    trainingExercise.breakBetweenSets()
            );
        }
    }
}
