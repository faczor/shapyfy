package com.shapyfy.core.boundary.api.dashboard.adapter;

import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;

import java.time.LocalDate;
import java.util.Optional;

public record DayContext(
        LocalDate date,
        Optional<PlanDay> planDay,
        Optional<ActivityLog> activityLog) {

    public static DayContext empty(LocalDate date) {
        return new DayContext(date, Optional.empty(), Optional.empty());
    }

    public static DayContext day(LocalDate date, PlanDay planDay) {
        return new DayContext(date, Optional.of(planDay), Optional.empty());
    }
}
