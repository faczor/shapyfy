package com.shapyfy.core.boundary.api.dashboard.adapter;

import com.shapyfy.core.domain.ActivityLogs;
import com.shapyfy.core.domain.TrainingPlanFetcher;
import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.domain.model.UserId;
import com.shapyfy.core.util.DateRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CalendarAdapter {

    private final TrainingPlanFetcher trainingPlanFetcher;

    private final ActivityLogs activityLogs;

    private final CalendarMapper calendarMapper;

    public Calendar fetchCalendar(LocalDate from, LocalDate to, UserId userId) {
        TrainingPlan trainingPlan = trainingPlanFetcher.fetchForUser(userId);
        List<ActivityLog> logs = activityLogs.fetchFor(from, to, userId);

        return calendarMapper.map(trainingPlan, logs, new DateRange(from, to));
    }

    public record Calendar(List<Day> days) {

        public record Day(
                LocalDate date,
                CalendarDayType type,
                String logId,
                String dayId) {
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
}
