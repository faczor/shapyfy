package com.sd.shapyfy.infrastructure.services.postgres.v2.converter;

import com.sd.shapyfy.domain.plan.ConfigurationDay;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.TrainingExercise;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.ExerciseEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.sd.shapyfy.domain.model.ConfigurationDayType.REST;
import static com.sd.shapyfy.domain.model.ConfigurationDayType.TRAINING;

@Component
@RequiredArgsConstructor
public class SessionToDomainConverter {

    private final ExerciseEntityToDomainConverter exerciseEntityToDomainConverter;

    //TODO Move TrainingExercise to converter (:
    public ConfigurationDay convertToConfigurationDay(TrainingDayEntity trainingDayEntity, SessionEntity sessionEntity) {

        return new ConfigurationDay(
                ConfigurationDayId.of(trainingDayEntity.getId()),
                trainingDayEntity.isOff() ? REST : TRAINING,
                trainingDayEntity.getName(),
                sessionEntity.getSessionExercises().stream()
                        .map(sessionExercise -> new TrainingExercise(exerciseEntityToDomainConverter.convert(
                                sessionExercise.getExercise()),
                                sessionExercise.getSetsAmount(),
                                sessionExercise.getRepsAmount(),
                                sessionExercise.getWeightAmount(), false))
                        .toList()
        );
    }
}
