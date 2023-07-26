package com.sd.shapyfy.boundary.api.trainingDays.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.boundary.api.exercises.contract.ExerciseDocument;
import com.sd.shapyfy.domain.session.Session;
import com.sd.shapyfy.domain.model.TrainingDay;

import java.util.List;

public record SelectedExercisesDocument(

        @JsonProperty(value = "items")
        List<SelectedExercise> selectedExercises) {

    public static SelectedExercisesDocument from(TrainingDay trainingDay) {
        return new SelectedExercisesDocument(trainingDay.mostCurrentSession().sessionExercises().stream().map(SelectedExercise::from).toList());
    }

    private record SelectedExercise(
            @JsonProperty(value = "exercise")
            ExerciseDocument exerciseDocument,
            //
            @JsonProperty(value = "exercise_training_attributes")
            ExerciseTraining exerciseTraining) {

        public static SelectedExercise from(Session.SessionExercise sessionExercise) {
            return new SelectedExercise(
                    ExerciseDocument.from(sessionExercise.exercise()),
                    new ExerciseTraining(
                            sessionExercise.sets(),
                            sessionExercise.reps(),
                            sessionExercise.getWeight().orElse(null)
                    )
            );
        }

        private record ExerciseTraining(
                @JsonProperty(value = "sets_amount", required = true)
                int sets,
                //
                @JsonProperty(value = "reps_amount", required = true)
                int reps,
                //
                @JsonProperty(value = "weight_amount")
                Double weight) {
        }
    }
}
