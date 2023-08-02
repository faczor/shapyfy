package com.sd.shapyfy.infrastructure.services.postgres.sessions.converter;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.domain.configuration.model.TrainingExercise;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.converter.ExerciseToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.sd.shapyfy.domain.configuration.model.ConfigurationDayType.REST;
import static com.sd.shapyfy.domain.configuration.model.ConfigurationDayType.TRAINING;

@Component
@RequiredArgsConstructor
public class SessionToDomainConverter {

    private final ExerciseToDomainConverter exerciseToDomainConverter;

    //TODO Move TrainingExercise to converter (:
    public ConfigurationDay convertToConfigurationDay(TrainingDayEntity trainingDayEntity, SessionEntity sessionEntity) {

        return new ConfigurationDay(
                ConfigurationDayId.of(trainingDayEntity.getId()),
                trainingDayEntity.isOff() ? REST : TRAINING,
                trainingDayEntity.getName(),
                sessionEntity.getSessionExercises().stream()
                        .map(sessionExercise -> new TrainingExercise(exerciseToDomainConverter.convert(
                                sessionExercise.getExercise()),
                                sessionExercise.getSetsAmount(),
                                sessionExercise.getRepsAmount(),
                                sessionExercise.getWeightAmount(), false))
                        .toList()
        );
    }
}
