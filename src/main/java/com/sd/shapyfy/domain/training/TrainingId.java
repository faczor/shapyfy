package com.sd.shapyfy.domain.training;


import lombok.Getter;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class TrainingId {

    UUID value;

    public static TrainingId of(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "TrainingId::" + value;
    }
}
