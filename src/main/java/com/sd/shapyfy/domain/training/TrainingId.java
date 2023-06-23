package com.sd.shapyfy.domain.training;

import lombok.Value;

@Value(staticConstructor = "of")
public class TrainingId {

    String value;

    @Override
    public String toString() {
        return "TrainingId::" + value;
    }
}
