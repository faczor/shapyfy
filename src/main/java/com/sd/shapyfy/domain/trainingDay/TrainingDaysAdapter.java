package com.sd.shapyfy.domain.trainingDay;

import com.sd.shapyfy.domain.exercises.ExerciseId;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.user.UserId;
import lombok.Value;

import java.util.List;
import java.util.Optional;

public interface TrainingDaysAdapter {

    Training.TrainingDay selectExercises(UserId userId, TrainingDayId trainingDayId, List<ExerciseDetails> selectedExercises);

    @Value
    class ExerciseDetails {
        ExerciseId exerciseId;

        int sets;

        int reps;

        Double weight;

        public Optional<Double> getWeight() {
            return Optional.ofNullable(weight);
        }
    }

}
