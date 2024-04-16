package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.UserId;
import com.shapyfy.core.domain.port.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityLogs {

    private final ActivityLogRepository activityLogRepository;

    public void skip(LocalDate date) {
        log.info("Attempt to skip track {}", date);
    }

    public void move(LocalDate from, LocalDate to) {
        log.info("Attempt to move track from {} to {}", from, to);
    }

    public void workout(LocalDate date) {
        log.info("Attempt to workout track {}", date);
    }

    public List<ActivityLog> fetchFor(LocalDate from, LocalDate to, UserId userId) {
        log.info("Fetching activity logs for {} from {} to {}", userId, from, to);

        return activityLogRepository.findAll(from, to, userId);
    }
}
