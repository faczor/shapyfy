package com.sd.shapyfy.infrastructure.services.postgres.sessions.converter;

import com.sd.shapyfy.domain.model.Session.SessionExercise;
import com.sd.shapyfy.domain.model.SessionExerciseId;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.ExerciseEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionExerciseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionExerciseEntityToDomainConverter {

    private final ExerciseEntityToDomainConverter exerciseEntityToDomainConverter;

    public SessionExercise convert(SessionExerciseEntity sessionExerciseEntity) {
        return new SessionExercise(
                SessionExerciseId.of(sessionExerciseEntity.getId()),
                sessionExerciseEntity.getSetsAmount(),
                sessionExerciseEntity.getRepsAmount(),
                sessionExerciseEntity.getWeightAmount(),
                exerciseEntityToDomainConverter.convert(sessionExerciseEntity.getExercise())
        );
    }
}
