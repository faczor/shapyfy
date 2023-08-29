package com.sd.shapyfy.infrastructure.services.postgres.training_day.converter;

import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.configuration.model.TrainingExercise;
import com.sd.shapyfy.domain.exercise.model.Exercise;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.plan.model.SessionPartId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingDayToDomainConverter {


    public ConfigurationDay toConfiguration(SessionPartEntity sessionPart) {
        return new ConfigurationDay(
                SessionPartId.of(sessionPart.getId()),
                sessionPart.getType(),
                sessionPart.getName(),
                sessionPart.getSessionExercises().stream().map(this::convert).toList());
    }

    public SessionPart toSession(TrainingConfiguration configuration, SessionPartEntity sessionPartEntity) {
        SessionPartId configurationId = configuration.configurationDays().stream().filter(day -> day.name().equals(sessionPartEntity.getName())).findFirst().map(ConfigurationDay::id).orElseThrow();
        return new SessionPart(
                SessionPartId.of(sessionPartEntity.getSession().getId()),
                configurationId,
                sessionPartEntity.getType(),
                sessionPartEntity.getDate(),
                sessionPartEntity.getSessionExercises().stream().map(this::convert).toList()
        );
    }

    //TODO move to proper converter
    private TrainingExercise convert(SessionExerciseEntity sessionExercise) {
        return new TrainingExercise(
                new Exercise(ExerciseId.of(sessionExercise.getExercise().getId()), sessionExercise.getExercise().getName()),
                sessionExercise.getSetsAmount(),
                sessionExercise.getRepsAmount(),
                sessionExercise.getRestBetweenSets(),
                sessionExercise.getWeightAmount(),
                sessionExercise.isFinished()
        );
    }
}
