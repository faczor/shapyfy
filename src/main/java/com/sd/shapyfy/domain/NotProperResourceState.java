package com.sd.shapyfy.domain;

public class NotProperResourceState extends RuntimeException{

    public NotProperResourceState(String message) {
        super(message);
    }
}
