package com.sd.shapyfy.domain.session.model.exception;

import com.sd.shapyfy.domain.trainingDay.TrainingDayId;

public class DraftSessionNotFound extends SessionNotFound {
    public DraftSessionNotFound(TrainingDayId id) {
        super(String.format("Draft session not found %s", id));
    }
}
