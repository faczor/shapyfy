package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.model.Exercise;

import java.util.List;

public interface ExercisePort {

    List<Exercise> fetchExercises();
}
