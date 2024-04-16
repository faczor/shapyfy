package com.shapyfy.core.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityLogs {

    public void skip(LocalDate date) {
        log.info("Attempt to skip track {}", date);
    }

    public void move(LocalDate from, LocalDate to) {
        log.info("Attempt to move track from {} to {}", from, to);
    }

    public void workout(LocalDate date) {
        log.info("Attempt to workout track {}", date);
    }
}
