package com.sd.shapyfy.domain.session.model;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class SessionPartId {

    UUID value;

    public static SessionPartId of(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "SessionId::" + value;
    }
}
