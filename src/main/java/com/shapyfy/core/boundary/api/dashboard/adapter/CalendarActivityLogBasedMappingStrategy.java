package com.shapyfy.core.boundary.api.dashboard.adapter;

import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.util.DateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//TODO refactor
public class CalendarActivityLogBasedMappingStrategy extends CalendarMappingStrategy {

    @Override
    List<DayContext> map(TrainingPlan trainingPlan, List<ActivityLog> activityLogs, DateRange dateRange) {
        ActivityLog firstLog = activityLogs.stream().min(Comparator.comparing(ActivityLog::date)).orElseThrow();
        List<DayContext> resultDays = new ArrayList<>();

        boolean isFirstLogAfterStartDate = firstLog.date().isAfter(dateRange.start());
        if (isFirstLogAfterStartDate) {
            DateRange rangeFromStartToFirstLog = new DateRange(dateRange.start(), firstLog.date().minusDays(1));
            PlanDay currentDay = firstLog.planDay();
            for (LocalDate date : rangeFromStartToFirstLog.listDatesWithinRange().reversed()) {

                DayContext calendarDayWithPlanDay = activityLogs.stream().filter(log -> log.date().isEqual(date)).findFirst()
                        .map(log -> calendarDayBasedOnActivityLog(date, log))
                        .orElse(calendarDayBasedOnPreviousDay(date, trainingPlan, currentDay));

                resultDays.add(calendarDayWithPlanDay);
                currentDay = calendarDayWithPlanDay.planDay().get();
            }

            dateRange = new DateRange(firstLog.date(), dateRange.end());
        }

        resultDays.addAll(futureDayContexts(dateRange, trainingPlan, activityLogs, firstLog.planDay()));

        return resultDays;
    }
}
