package com.sd.shapyfy.domain.model;


import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class PlanId {

    UUID value;

    public static PlanId of(String value) {
        return of(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return "PlanId::" + value;
    }
}
