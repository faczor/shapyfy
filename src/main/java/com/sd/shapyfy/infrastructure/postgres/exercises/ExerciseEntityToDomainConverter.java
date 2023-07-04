package com.sd.shapyfy.infrastructure.postgres.exercises;

import com.sd.shapyfy.domain.exercises.Exercise;
import com.sd.shapyfy.domain.exercises.ExerciseId;
import org.springframework.stereotype.Component;

@Component
public class ExerciseEntityToDomainConverter {

    public Exercise convert(ExerciseEntity entity) {
        return new Exercise(
                ExerciseId.of(entity.getId()),
                entity.getName()
        );
    }
}