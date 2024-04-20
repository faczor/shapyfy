package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.Exercise;
import com.shapyfy.core.domain.port.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExercisesAdapter implements ExerciseRepository {

    private final ExerciseJpaRepository exerciseJpaRepository;

    @Override
    public Exercise getById(Exercise.ExerciseId exerciseId) {
        return exerciseJpaRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found for id " + exerciseId.getId()));
    }

    @Override
    public Exercise save(Exercise exercise) {
        return exerciseJpaRepository.save(exercise);
    }

    @Override
    public List<Exercise> findAll() {
        return exerciseJpaRepository.findAll();
    }
}
