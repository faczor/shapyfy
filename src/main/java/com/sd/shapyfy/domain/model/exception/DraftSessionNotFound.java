package com.sd.shapyfy.domain.model.exception;

import com.sd.shapyfy.domain.model.TrainingDayId;

public class DraftSessionNotFound extends SessionNotFound {
    public DraftSessionNotFound(TrainingDayId id) {
        super(String.format("Draft session not found %s", id));
    }
}
