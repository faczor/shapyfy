package com.sd.shapyfy.infrastructure.services.postgres.v2.converter;

import com.sd.shapyfy.domain.model.Exercise;
import com.sd.shapyfy.domain.model.ExerciseId;
import com.sd.shapyfy.domain.plan.ConfigurationDay;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.TrainingExercise;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sd.shapyfy.domain.model.ConfigurationDayType.REST;
import static com.sd.shapyfy.domain.model.ConfigurationDayType.TRAINING;

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
                sessionExercise.getWeightAmount(),
                sessionExercise.isFinished()
        );
    }
}
