package com.sd.shapyfy.domain.session.model.exception;

import com.sd.shapyfy.domain.NotProperResourceState;

public class SessionNotFound extends NotProperResourceState {
    public SessionNotFound(String message) {
        super(message);
    }
}
