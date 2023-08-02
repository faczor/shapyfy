package com.sd.shapyfy.domain.configuration.exception;

import com.sd.shapyfy.domain.InvalidDomainResourceState;

public class NotFoundDayInConfiguration extends InvalidDomainResourceState {
    public NotFoundDayInConfiguration(String message) {
        super(message);
    }
}
