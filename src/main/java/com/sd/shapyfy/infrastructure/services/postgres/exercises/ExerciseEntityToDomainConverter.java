package com.sd.shapyfy.infrastructure.services.postgres.exercises;

import com.sd.shapyfy.domain.model.Exercise;
import com.sd.shapyfy.domain.model.ExerciseId;
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