package com.sd.shapyfy.domain.exercise;

import com.sd.shapyfy.boundary.api.exercises.Creator;
import com.sd.shapyfy.domain.exercise.model.Exercise;

public interface ExerciseService {
    Exercise create(String name, Creator creator);
}
