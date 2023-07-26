package com.sd.shapyfy.domain.session;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class SessionExerciseId {

    UUID value;

    public static SessionExerciseId of(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "SessionId::" + value;
    }
}
