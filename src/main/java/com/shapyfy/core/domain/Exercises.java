package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.Exercise;
import com.shapyfy.core.domain.model.MuscleGroup;
import com.shapyfy.core.domain.port.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class Exercises {

    private final ExerciseRepository exerciseRepository;

    public Exercise create(String name) {
        log.info("Attempt to create exercise {}", name);
        Exercise exercise = exerciseRepository.save(new Exercise(null, name));
        log.info("Exercise created {}", exercise);
        return exercise;
    }

    public Exercise fetchById(Exercise.ExerciseId exerciseId) {
        return exerciseRepository.getById(exerciseId);
    }
}
