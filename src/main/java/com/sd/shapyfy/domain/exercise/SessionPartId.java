package com.sd.shapyfy.domain.exercise;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class SessionPartId {

    UUID value;

    @Override
    public String toString() {
        return "SessionPartId::" + value;
    }
}
