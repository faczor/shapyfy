package com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter;

import com.sd.shapyfy.domain.exercise.model.Exercise;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.domain.configuration.model.TrainingExercise;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sd.shapyfy.domain.configuration.model.ConfigurationDayType.REST;
import static com.sd.shapyfy.domain.configuration.model.ConfigurationDayType.TRAINING;

@Component
@RequiredArgsConstructor
public class TrainingDayToDomainConverter {


    public ConfigurationDay toConfiguration(TrainingDayEntity trainingDayEntity) {
        return new ConfigurationDay(
                ConfigurationDayId.of(trainingDayEntity.getId()),
                trainingDayEntity.isOff() ? REST : TRAINING,
                trainingDayEntity.getName(),
                trainingDayEntity.getMostCurrentSession()
                        .map(mostCurrentSession -> mostCurrentSession.getSessionExercises().stream().map(this::convert).toList())
                        .orElse(List.of())
        );
    }

    private TrainingExercise convert(SessionExerciseEntity sessionExercise) {
        return new TrainingExercise(
                new Exercise(ExerciseId.of(sessionExercise.getExercise().getId()), sessionExercise.getExercise().getName()),
                sessionExercise.getSetsAmount(),
                sessionExercise.getRepsAmount(),
                10, //TODO breakBetweenStes
                sessionExercise.getWeightAmount(),

                sessionExercise.isFinished()
        );
    }
}
