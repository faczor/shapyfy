package com.sd.shapyfy.infrastructure.services.postgres.configuration.rq_models;

import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.util.List;

public record CreateConfigurationParams(
        String name,
        List<String> exerciseAttributes,
        List<String> setAttributes,
        List<SessionDayConfiguration> sessionDayConfigurations) {

    public record SessionDayConfiguration(
            String name,
            SessionPartType dayType,
            List<SelectedExercise> selectedExercises) {

        public record SelectedExercise(
                ExerciseEntity exercise,
                int sets,
                int reps,
                Double weight,
                int secondRestBetweenSets) {
        }
    }
}
