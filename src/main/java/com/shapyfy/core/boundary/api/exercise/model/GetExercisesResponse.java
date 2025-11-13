package com.shapyfy.core.boundary.api.exercise.model;

import java.util.List;

public record GetExercisesResponse(
        List<ExerciseContract> exercises
) {
}
