package com.sd.shapyfy.boundary.api.trainings.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.TrainingLookup;
import com.sd.shapyfy.domain.model.Session;
import com.sd.shapyfy.domain.model.TrainingDayType;

import java.time.DayOfWeek;
import java.util.List;

public record CurrentTrainingResponseDocument(

        @JsonProperty(value = "training_id", required = true)
        String id,
        //
        @JsonProperty(value = "name", required = true)
        String name,
        //
        @JsonProperty(value = "training_days", required = true)
        List<DayDocument> days
) {

    public static CurrentTrainingResponseDocument from(TrainingLookup.CurrentTraining currentTraining) {
        return new CurrentTrainingResponseDocument(
                currentTraining.trainingId().getValue().toString(),
                currentTraining.name(),
                currentTraining.days().stream().map(DayDocument::from).toList()
        );
    }

    public record DayDocument(
            @JsonProperty(value = "training_day_id", required = true)
            String id,
            //
            @JsonProperty(value = "name", required = true)
            String name,
            //
            @JsonProperty(value = "day_type", required = true)
            TrainingDayType dayType,
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
                    day.dayType() == TrainingDayType.OFF ? List.of() : day.session().getSessionExercises().stream().map(TrainingExercise::from).toList()
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
                        sessionExercise.getExercise().getId().getValue().toString(),
                        sessionExercise.getExercise().getName(),
                        sessionExercise.getSets(),
                        sessionExercise.getReps(),
                        sessionExercise.getWeight().orElse(null)
                );
            }
        }
    }
}
