package com.sd.shapyfy.infrastructure.services.postgres;

public class ResourceNotFound extends RuntimeException {

    public ResourceNotFound(String message) {
        super(message);
    }
}
