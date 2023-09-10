package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.domain.configuration.model.*;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationAttributeEntity;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationPartEntity;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationPartExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.converter.ExerciseToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingEntityToDomainConverter {

    private final TrainingPlanToDomainConverter trainingPlanToDomainConverter;

    private final ExerciseToDomainConverter exerciseToDomainConverter;

    //TODO split between converters move to configuration converters
    public TrainingConfiguration convertToConfiguration(TrainingEntity trainingEntity) {

        return new TrainingConfiguration(
                trainingPlanToDomainConverter.convert(trainingEntity),
                ConfigurationId.of(trainingEntity.getConfiguration().getId()),
                trainingEntity.getConfiguration().getAttributes().stream().map(this::attributeToDomainConfiguration).toList(),
                trainingEntity.getConfiguration().getParts().stream().map(this::convertPartToDomain).toList()
        );
    }

    private ConfigurationAttribute attributeToDomainConfiguration(ConfigurationAttributeEntity configurationAttributeEntity) {
        return new ConfigurationAttribute(
                ConfigurationAttributeId.of(configurationAttributeEntity.getId()),
                configurationAttributeEntity.getName(),
                configurationAttributeEntity.getConfigurationAttributeType()
        );
    }

    public ConfigurationDay convertPartToDomain(ConfigurationPartEntity configurationPart) {
        return new ConfigurationDay(
                ConfigurationDayId.of(configurationPart.getId()),
                configurationPart.getType(),
                configurationPart.getName(),
                configurationPart.getExercises().stream().map(this::convertExerciseToDomain).toList()
        );
    }

    private ConfiguredExercises convertExerciseToDomain(ConfigurationPartExerciseEntity exercise) {
        return new ConfiguredExercises(
                TrainingExerciseId.of(exercise.getId()),
                exerciseToDomainConverter.convert(exercise.getExercise()),
                exercise.getSetsAmount(),
                exercise.getRepsAmount(),
                exercise.getRestBetweenSets(),
                exercise.getWeightAmount()
        );
    }
}
