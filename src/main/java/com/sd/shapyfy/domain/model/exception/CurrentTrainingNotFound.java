package com.sd.shapyfy.domain.model.exception;

import com.sd.shapyfy.domain.NotProperResourceState;
import com.sd.shapyfy.domain.model.UserId;

public class CurrentTrainingNotFound extends NotProperResourceState {
    public CurrentTrainingNotFound(UserId userId) {
        super(String.format("Current training not found for %s", userId));
    }
}
