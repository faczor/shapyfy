package com.sd.shapyfy.domain.exercise;

import com.sd.shapyfy.domain.exercise.model.Exercise;

import java.util.List;

public interface ExerciseFetcher {

    List<Exercise> fetchExercises();
}
