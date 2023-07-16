package com.sd.shapyfy.boundary.api.trainingDays.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.boundary.api.exercises.contract.ExerciseDocument;
import com.sd.shapyfy.domain.session.model.Session;
import com.sd.shapyfy.domain.training.Training;

import java.util.List;

public record SelectedExercisesDocument(

        @JsonProperty(value = "items")
        List<SelectedExercise> selectedExercises) {

    public static SelectedExercisesDocument from(Training.TrainingDay trainingDay) {
        return new SelectedExercisesDocument(trainingDay.mostCurrentSession().getSessionExercises().stream().map(SelectedExercise::from).toList());
    }

    private record SelectedExercise(
            @JsonProperty(value = "exercise")
            ExerciseDocument exerciseDocument,
            //
            @JsonProperty(value = "exercise_training_attributes")
            ExerciseTraining exerciseTraining) {

        public static SelectedExercise from(Session.SessionExercise sessionExercise) {
            return new SelectedExercise(
                    ExerciseDocument.from(sessionExercise.getExercise()),
                    new ExerciseTraining(
                            sessionExercise.getSets(),
                            sessionExercise.getReps(),
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
