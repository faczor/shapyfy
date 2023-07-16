package com.sd.shapyfy.domain.session.model.exception;

import com.sd.shapyfy.domain.trainingDay.TrainingDayId;

public class ActiveSessionNotFound extends SessionNotFound {

    public ActiveSessionNotFound(TrainingDayId trainingDayId) {
        super(String.format("Active session not found for %s", trainingDayId));
    }
}
