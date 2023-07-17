package com.sd.shapyfy.domain.model.exception;

import com.sd.shapyfy.domain.model.TrainingDayId;

public class ActiveSessionNotFound extends SessionNotFound {

    public ActiveSessionNotFound(TrainingDayId trainingDayId) {
        super(String.format("Active session not found for %s", trainingDayId));
    }
}
