package com.sd.shapyfy.infrastructure.services.postgres.exercises.component;

import com.sd.shapyfy.domain.exercise.model.Exercise;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.exercise.ExerciseFetcher;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.ExerciseNotFound;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresExerciseFetcher implements ExerciseFetcher {

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
