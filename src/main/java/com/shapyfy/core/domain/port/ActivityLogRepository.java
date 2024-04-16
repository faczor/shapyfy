package com.shapyfy.core.domain.port;

import com.shapyfy.core.domain.ActivityLogs;
import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.UserId;

import java.time.LocalDate;
import java.util.List;

public interface ActivityLogRepository {
    List<ActivityLog> findAll(LocalDate from, LocalDate to, UserId userId);
}
