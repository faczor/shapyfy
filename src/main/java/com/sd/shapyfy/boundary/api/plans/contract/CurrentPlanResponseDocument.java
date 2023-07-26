package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.TrainingLookup;
import com.sd.shapyfy.domain.session.Session;
import com.sd.shapyfy.domain.model.ConfigurationDayType;

import java.time.DayOfWeek;
import java.util.List;

public record CurrentPlanResponseDocument(

        @JsonProperty(value = "id", required = true)
        String id,
        //
        @JsonProperty(value = "name", required = true)
        String name,
        //
        @JsonProperty(value = "training_days", required = true)
        List<DayDocument> days
) {

    public static CurrentPlanResponseDocument from(TrainingLookup.CurrentTraining currentTraining) {
        return new CurrentPlanResponseDocument(
                currentTraining.planId().getValue().toString(),
                currentTraining.name(),
                currentTraining.days().stream().map(DayDocument::from).toList()
        );
    }

    public record DayDocument(
            @JsonProperty(value = "id", required = true)
            String id,
            //
            @JsonProperty(value = "name", required = true)
            String name,
            //
            @JsonProperty(value = "day_type", required = true)
            ConfigurationDayType dayType,
            //
            @JsonProperty(value = "day_of_week", required = true)
            DayOfWeek dayOfWeek,
            //
            @JsonProperty(value = "training_exercises", required = true)
            List<TrainingExercise> trainingExercises
    ) {
        public static DayDocument from(TrainingLookup.CurrentTraining.Day day) {
            return new DayDocument(
                    day.trainingDayId().getValue().toString(),
                    day.name(),
                    day.dayType(),
                    day.dayOfWeek(),
                    day.dayType() == ConfigurationDayType.REST ? List.of() : day.session().sessionExercises().stream().map(TrainingExercise::from).toList()
            );
        }

        //TODO extract to common package
        public record TrainingExercise(

                @JsonProperty(value = "exercise_id", required = true)
                String exerciseId,
                //
                @JsonProperty(value = "name", required = true)
                String exerciseName,
                //
                @JsonProperty(value = "sets", required = true)
                int sets,
                //
                @JsonProperty(value = "reps", required = true)
                int reps,
                //
                @JsonProperty(value = "weight", required = false)
                Double weight
        ) {
            public static TrainingExercise from(Session.SessionExercise sessionExercise) {
                return new TrainingExercise(
                        sessionExercise.exercise().getId().getValue().toString(),
                        sessionExercise.exercise().getName(),
                        sessionExercise.sets(),
                        sessionExercise.reps(),
                        sessionExercise.getWeight().orElse(null)
                );
            }
        }
    }
}
