package com.sd.shapyfy.infrastructure.postgres.trainings;

import com.sd.shapyfy.infrastructure.postgres.ResourceNotFound;

public class TrainingNotFound extends ResourceNotFound {

    public TrainingNotFound(String value) {
        super(value);
    }
}
