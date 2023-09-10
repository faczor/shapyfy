package com.sd.shapyfy.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VersionDisplayer {

    @Value("${app.version}")
    private String version;

    @EventListener(ApplicationReadyEvent.class)
    void onApplicationReady() {
        log.info("Current app version {}", version);
    }
}
