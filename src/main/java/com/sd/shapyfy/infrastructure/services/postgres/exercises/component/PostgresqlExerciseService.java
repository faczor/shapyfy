package com.sd.shapyfy.infrastructure.services.postgres.exercises.component;

import com.sd.shapyfy.boundary.api.exercises.Creator;
import com.sd.shapyfy.domain.exercise.ExerciseService;
import com.sd.shapyfy.domain.exercise.model.Exercise;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.converter.ExerciseToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresqlExerciseService implements ExerciseService {

    private final PostgresExerciseRepository exerciseRepository;

    private final ExerciseToDomainConverter exerciseToDomainConverter;

    @Override
    public Exercise create(String name, Creator creator) {
        log.info("Attempt to create exercise {} creator {}", name, creator);
        ExerciseEntity createdExercise = exerciseRepository.save(ExerciseEntity.create(name, creator));
        log.info("Saved exercise {}", createdExercise);
        return exerciseToDomainConverter.convert(createdExercise);
    }
}
