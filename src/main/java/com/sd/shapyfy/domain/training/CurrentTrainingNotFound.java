package com.sd.shapyfy.domain.training;

import com.sd.shapyfy.domain.NotProperResourceState;
import com.sd.shapyfy.domain.user.UserId;

public class CurrentTrainingNotFound extends NotProperResourceState {
    public CurrentTrainingNotFound(UserId userId) {
        super(String.format("Current training not found for %s", userId));
    }
}
