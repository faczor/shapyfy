package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.*;
import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingManagementAdapter {

    Training create(TrainingInitialConfiguration initialConfiguration, UserId userId);

    TrainingDay exercisesSelection(TrainingDayId trainingDayId, List<SelectedExercise> selectedExercises, UserId userId);

    void activate(TrainingId trainingId, TrainingDayId startWithDayId, LocalDate startDate);


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
