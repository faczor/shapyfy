package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.exercises.ExerciseId;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.domain.trainingDay.TrainingDayType;
import com.sd.shapyfy.domain.user.UserId;
import lombok.Value;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface TrainingManagementAdapter {

    Training create(TrainingInitialConfiguration initialConfiguration, UserId userId);

    Training.TrainingDay exercisesSelection(TrainingDayId trainingDayId, List<SelectedExercise> selectedExercises, UserId userId);


    @Value
    class SelectedExercise {

        ExerciseId exerciseId;

        int sets;

        int reps;

        Double weight;

        public Optional<Double> getWeight() {
            return Optional.ofNullable(weight);
        }
    }

    @Value(staticConstructor = "of")
    class TrainingInitialConfiguration {
        String name;

        List<SessionDayConfiguration> sessionDayConfigurations;

        public Optional<String> getName() {
            return Optional.ofNullable(name);
        }

        @Value(staticConstructor = "of")
        public static class SessionDayConfiguration {
            String name;
            DayOfWeek dayOfWeek;
            TrainingDayType dayType;
        }
    }
}
