package com.shapyfy.core.boundary.api.exercise.adapter;

import com.shapyfy.core.boundary.api.exercise.model.CreateExerciseRequest;
import com.shapyfy.core.boundary.api.exercise.model.ExerciseContract;
import com.shapyfy.core.domain.Exercises;
import com.shapyfy.core.domain.model.Exercise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExerciseApiAdapter {

    private final Exercises exercises;

    public Exercise createExercise(CreateExerciseRequest request) {
        log.info("Creating exercise {}", request);
        return exercises.create(request.name());
    }

    public ExerciseContract fetchExercise(UUID exerciseId) {
        log.info("Fetching exercise {}", exerciseId);
        Exercise exercise = exercises.fetchById(Exercise.ExerciseId.of(exerciseId));

        return ExerciseContract.from(exercise);
    }

    public List<ExerciseContract> fetchExercises() {
        List<Exercise> fetchedExercises = exercises.fetchAll();

        return fetchedExercises.stream()
                .map(ExerciseContract::from)
                .toList();
    }
}
