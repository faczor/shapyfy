package com.sd.shapyfy.domain.configuration.exception;

public class UserNotOwningResourceException extends RuntimeException {

    public UserNotOwningResourceException(String message) {
        super(message);
    }
}
