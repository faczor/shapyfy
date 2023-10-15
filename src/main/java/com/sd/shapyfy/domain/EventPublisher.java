package com.sd.shapyfy.domain;

import org.springframework.context.ApplicationEvent;

public interface EventPublisher {

    void publish(ApplicationEvent event);
}
