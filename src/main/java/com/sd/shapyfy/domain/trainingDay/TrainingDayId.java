package com.sd.shapyfy.domain.trainingDay;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class TrainingDayId {

    UUID value;

    public TrainingDayId of(String value) {
        return TrainingDayId.of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "TrainingDayId::" + value;
    }
}
