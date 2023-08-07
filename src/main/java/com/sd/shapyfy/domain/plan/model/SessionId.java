package com.sd.shapyfy.domain.plan.model;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class SessionId {

    UUID value;

    public static SessionId of(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "SessionId::" + value;
    }
}
