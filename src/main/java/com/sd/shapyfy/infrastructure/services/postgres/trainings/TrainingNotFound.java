package com.sd.shapyfy.infrastructure.services.postgres.trainings;

import com.sd.shapyfy.infrastructure.services.postgres.ResourceNotFound;

public class TrainingNotFound extends ResourceNotFound {

    public TrainingNotFound(String value) {
        super(value);
    }
}
