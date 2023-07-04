package com.sd.shapyfy.infrastructure.postgres.exercises;

import com.sd.shapyfy.infrastructure.postgres.ResourceNotFound;

public class ExerciseNotFound extends ResourceNotFound {
    public ExerciseNotFound(String message) {
        super(message);
    }
}
