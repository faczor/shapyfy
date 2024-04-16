package com.shapyfy.core.domain.port;

import com.shapyfy.core.domain.model.Exercise;

import java.util.List;

public interface ExerciseRepository {

    Exercise getById(Exercise.ExerciseId exerciseId);

    Exercise save(Exercise exercise);

    List<Exercise> findAll();
}
