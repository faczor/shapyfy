package com.shapyfy.core.boundary.api.dashboard.adapter;

import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.util.DateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CalendarMappingStrategy {

    abstract List<DayContext> map(TrainingPlan trainingPlan, List<ActivityLog> activityLogs, DateRange dateRange);

    protected List<DayContext> futureDayContexts(DateRange dateRange, TrainingPlan trainingPlan, List<ActivityLog> activityLogs, PlanDay currentDay) {
        List<DayContext> resultDays = new ArrayList<>();
        for (LocalDate date : dateRange.listDatesWithinRange()) {

            DayContext calendarDayWithPlanDay = activityLogs.stream().filter(log -> log.date().isEqual(date)).findFirst()
                    .map(log -> calendarDayBasedOnActivityLog(date, log))
                    .orElse(calendarDayBasedOnNextDay(date, trainingPlan, currentDay));

            resultDays.add(calendarDayWithPlanDay);
            currentDay = calendarDayWithPlanDay.planDay().get();
        }

        return resultDays;
    }

    protected DayContext calendarDayBasedOnActivityLog(LocalDate date, ActivityLog log) {
        return new DayContext(date, Optional.of(log.planDay()), Optional.of(log));
    }

    protected DayContext calendarDayBasedOnPreviousDay(LocalDate date, TrainingPlan trainingPlan, PlanDay planDay) {
        return calendarDayBasedOnPlanDay(date, trainingPlan.dayBefore(planDay));
    }

    private DayContext calendarDayBasedOnNextDay(LocalDate date, TrainingPlan trainingPlan, PlanDay planDay) {
        return calendarDayBasedOnPlanDay(date, trainingPlan.nextDay(planDay));
    }

    private DayContext calendarDayBasedOnPlanDay(LocalDate date, PlanDay planDay) {
        return new DayContext(date, Optional.of(planDay), Optional.empty());
    }
}
