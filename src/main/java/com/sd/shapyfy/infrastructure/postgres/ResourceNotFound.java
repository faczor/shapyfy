package com.sd.shapyfy.infrastructure.postgres;

public class ResourceNotFound extends RuntimeException {

    public ResourceNotFound(String message) {
        super(message);
    }
}
