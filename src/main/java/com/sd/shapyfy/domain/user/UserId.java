package com.sd.shapyfy.domain.user;

import lombok.Value;

@Value(staticConstructor = "of")
public class UserId {

    String value;

    @Override
    public String toString() {
        return "UserId::" + this.value;
    }
}
