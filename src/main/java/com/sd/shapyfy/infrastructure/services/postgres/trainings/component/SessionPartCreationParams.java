package com.sd.shapyfy.infrastructure.services.postgres.trainings.component;

import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationAttributeEntity;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SessionPartCreationParams(
        String name,
        SessionPartType type,
        SessionPartState state,
        LocalDate date,
        UUID configurationPartId,
        List<SelectedExercisesParams> selectedExercisesPrams) {
    public record SelectedExercisesParams(
            ExerciseEntity exercise,
            int restBetweenSets,
            List<ConfigurationAttributeEntity> attributes,
            List<SetConfiguration> setConfigurations) {
        public record SetConfiguration(
                Integer repsAmount,
                Double weightAmount,
                Boolean isFinished,
                List<ConfigurationAttributeEntity> attributes) {
        }
    }
}
