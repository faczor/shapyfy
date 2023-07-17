package com.sd.shapyfy.domain.model;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class TrainingDayId {

    UUID value;

    public static TrainingDayId of(String value) {
        return TrainingDayId.of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "TrainingDayId::" + value;
    }
}
