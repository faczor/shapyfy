package com.sd.shapyfy.domain;

public class InvalidDomainResourceState extends RuntimeException{

    public InvalidDomainResourceState(String message) {
        super(message);
    }
}
