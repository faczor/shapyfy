package com.sd.shapyfy.infrastructure.services.postgres.exercises;

import com.sd.shapyfy.domain.exercises.Exercise;
import com.sd.shapyfy.domain.exercises.ExerciseId;
import com.sd.shapyfy.domain.exercises.ExercisePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresExercisePort implements ExercisePort {

    private final PostgresExerciseRepository exerciseRepository;

    @Override
    public List<Exercise> fetchExercises() {
        return exerciseRepository.findAll().stream().map(this::convert).toList();
    }

    public ExerciseEntity fetchFor(ExerciseId id) {
        return exerciseRepository.findById(id.getValue()).orElseThrow(() -> new ExerciseNotFound("Exercise not found with id::" + id));
    }

    private Exercise convert(ExerciseEntity entity) {
        return new Exercise(ExerciseId.of(entity.getId()), entity.getName());
    }
}
