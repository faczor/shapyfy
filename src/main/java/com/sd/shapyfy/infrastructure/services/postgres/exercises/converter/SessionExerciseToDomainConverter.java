package com.sd.shapyfy.infrastructure.services.postgres.exercises.converter;

import com.sd.shapyfy.domain.configuration.model.ExerciseSet;
import com.sd.shapyfy.domain.configuration.model.ExerciseSetId;
import com.sd.shapyfy.domain.configuration.model.TrainingExerciseId;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseSetEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionExerciseToDomainConverter {

    private final ExerciseToDomainConverter exerciseToDomainConverter;

    private final AttributeToDomainConverter attributeToDomainConverter;

    public TrainingExercise convert(SessionExerciseEntity sessionExerciseEntity) {
        return new TrainingExercise(
                TrainingExerciseId.of(sessionExerciseEntity.getId()),
                exerciseToDomainConverter.convert(sessionExerciseEntity.getExercise()),
                sessionExerciseEntity.getRestBetweenSets(),
                sessionExerciseEntity.isFinished(),
                sessionExerciseEntity.getAttributes().stream().map(attributeToDomainConverter::convert).toList(),
                sessionExerciseEntity.getSets().stream().map(this::convert).toList()
        );
    }

    private ExerciseSet convert(SessionExerciseSetEntity setEntity) {
        return new ExerciseSet(
                ExerciseSetId.of(setEntity.getId()),
                setEntity.getRepsAmount(),
                setEntity.getWeightAmount(),
                setEntity.isFinished(),
                setEntity.getAttributes().stream().map(attributeToDomainConverter::convert).toList()
        );
    }
}
