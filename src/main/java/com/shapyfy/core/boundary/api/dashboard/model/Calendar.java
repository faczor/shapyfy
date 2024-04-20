package com.shapyfy.core.boundary.api.dashboard.model;

import com.shapyfy.core.util.DateRange;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record Calendar(List<Day> days) {

    public record Day(
            LocalDate date,
            CalendarDayType type,
            UUID logId,
            UUID dayId) {
    }

    public enum CalendarDayType {
        REST,
        WORKOUT,
        HISTORICAL,
        UNKNOWN
    }

    public static Calendar empty(DateRange dateRange) {
        return new Calendar(
                dateRange.streamDatesWithinRange().map(date -> new Day(
                        date,
                        CalendarDayType.UNKNOWN,
                        null,
                        null
                )).toList()
        );
    }
}
