package com.sd.shapyfy.infrastructure.services.postgres.exercises;

import com.sd.shapyfy.infrastructure.services.postgres.ResourceNotFound;

public class ExerciseNotFound extends ResourceNotFound {
    public ExerciseNotFound(String message) {
        super(message);
    }
}
