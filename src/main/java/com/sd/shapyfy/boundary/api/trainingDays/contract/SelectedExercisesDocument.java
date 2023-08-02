package com.sd.shapyfy.boundary.api.trainingDays.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.boundary.api.exercises.contract.ExerciseDocument;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.TrainingExercise;

import java.util.List;

public record SelectedExercisesDocument(

        @JsonProperty(value = "items")
        List<SelectedExercise> selectedExercises) {

    public static SelectedExercisesDocument from(ConfigurationDay trainingDay) {
        return new SelectedExercisesDocument(trainingDay.exercises().stream().map(SelectedExercise::from).toList());
    }


    private record SelectedExercise(
            @JsonProperty(value = "exercise")
            ExerciseDocument exerciseDocument,
            //
            @JsonProperty(value = "exercise_training_attributes")
            ExerciseTraining exerciseTraining) {

        public static SelectedExercise from(TrainingExercise trainingExercise) {
            return new SelectedExercise(
                    ExerciseDocument.from(trainingExercise.exercise()),
                    new ExerciseTraining(
                            trainingExercise.sets(),
                            trainingExercise.reps(),
                            trainingExercise.weight()
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
