package com.sd.shapyfy.infrastructure.services.postgres.trainingDay;

import com.sd.shapyfy.infrastructure.services.postgres.ResourceNotFound;

public class TrainingDayNotFound extends ResourceNotFound {
    public TrainingDayNotFound(String message) {
        super(message);
    }
}
