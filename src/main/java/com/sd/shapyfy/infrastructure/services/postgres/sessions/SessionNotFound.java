package com.sd.shapyfy.infrastructure.services.postgres.sessions;

import com.sd.shapyfy.infrastructure.services.postgres.ResourceNotFound;

public class SessionNotFound extends ResourceNotFound {
    public SessionNotFound(String message) {
        super(message);
    }
}
