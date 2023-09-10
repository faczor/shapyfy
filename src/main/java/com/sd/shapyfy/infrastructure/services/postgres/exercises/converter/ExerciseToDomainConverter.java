package com.sd.shapyfy.infrastructure.services.postgres.exercises.converter;

import com.sd.shapyfy.domain.exercise.model.Exercise;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExerciseToDomainConverter {

    public Exercise convert(ExerciseEntity entity) {
        return new Exercise(
                ExerciseId.of(entity.getId()),
                entity.getName()
        );
    }
}