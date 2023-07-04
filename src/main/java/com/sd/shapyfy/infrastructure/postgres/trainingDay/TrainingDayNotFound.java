package com.sd.shapyfy.infrastructure.postgres.trainingDay;

import com.sd.shapyfy.infrastructure.postgres.ResourceNotFound;

public class TrainingDayNotFound extends ResourceNotFound {
    public TrainingDayNotFound(String message) {
        super(message);
    }
}
