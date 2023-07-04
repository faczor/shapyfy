package com.sd.shapyfy.domain;

public class UserNotOwningResourceException extends RuntimeException {

    public UserNotOwningResourceException(String message) {
        super(message);
    }
}
