package com.shapyfy.core.boundary.api.dashboard.adapter;

import com.shapyfy.core.boundary.api.dashboard.adapter.CalendarAdapter.Calendar;
import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.util.DateRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;

@Slf4j
@Component
class CalendarMapper {

    public Calendar map(TrainingPlan trainingPlan, List<ActivityLog> activityLogs, DateRange dateRange) {
        log.info("Mapping training plan {} to calendar for {}", trainingPlan, dateRange);

        if (FALSE.equals(trainingPlan.isActive())) {
            return Calendar.empty(dateRange);
        }

        boolean activityLogCloserThanStartDate = isActivityLogCloserThanStartDate(activityLogs, trainingPlan.startDate(), dateRange.start());

        return activityLogCloserThanStartDate
                ? calendarBuildStrategyByActivityLogDate(trainingPlan, activityLogs, dateRange)
                : calendarBuildStrategyByPlanDate(trainingPlan, activityLogs, dateRange);
    }

    private Calendar calendarBuildStrategyByPlanDate(TrainingPlan trainingPlan, List<ActivityLog> activityLogs, DateRange dateRange) {
        List<Calendar.Day> resultDays = new ArrayList<>();

        if (dateRange.start().isBefore(trainingPlan.startDate())) {
            DateRange rangeFromStartToFirstLog = new DateRange(dateRange.start(), trainingPlan.startDate().minusDays(1));
            resultDays.addAll(rangeFromStartToFirstLog.streamDatesWithinRange().map(date -> new Calendar.Day(date, Calendar.CalendarDayType.UNKNOWN, null, null)).toList());
            dateRange = new DateRange(trainingPlan.startDate(), dateRange.end());
        }

        PlanDay currentDay = trainingPlan.days().getFirst();
        for (LocalDate date : dateRange.listDatesWithinRange()) {

            CalendarDayWithPlanDay calendarDayWithPlanDay = activityLogs.stream().filter(log -> log.date().isEqual(date)).findFirst()
                    .map(log -> calendarDayBasedOnActivityLog(date, log))
                    .orElse(calendarDayBasedOnNextDay(date, trainingPlan, currentDay));

            resultDays.add(calendarDayWithPlanDay.calendarDay);
            currentDay = calendarDayWithPlanDay.planDay;
        }

        return new Calendar(resultDays);
    }

    private Calendar calendarBuildStrategyByActivityLogDate(TrainingPlan trainingPlan, List<ActivityLog> activityLogs, DateRange dateRange) {
        ActivityLog firstLog = activityLogs.stream().min(Comparator.comparing(ActivityLog::date)).orElseThrow();
        List<Calendar.Day> resultDays = new ArrayList<>();

        boolean isFirstLogAfterStartDate = firstLog.date().isAfter(dateRange.start());
        if (isFirstLogAfterStartDate) {
            DateRange rangeFromStartToFirstLog = new DateRange(dateRange.start(), firstLog.date().minusDays(1));
            PlanDay currentDay = firstLog.planDay();
            for (LocalDate date : rangeFromStartToFirstLog.listDatesWithinRange().reversed()) {

                CalendarDayWithPlanDay calendarDayWithPlanDay = activityLogs.stream().filter(log -> log.date().isEqual(date)).findFirst()
                        .map(log -> calendarDayBasedOnActivityLog(date, log))
                        .orElse(calendarDayBasedOnPreviousDay(date, trainingPlan, currentDay));

                resultDays.add(calendarDayWithPlanDay.calendarDay);
                currentDay = calendarDayWithPlanDay.planDay;
            }

            dateRange = new DateRange(firstLog.date(), dateRange.end());
        }

        PlanDay currentDay = firstLog.planDay();
        for (LocalDate date : dateRange.listDatesWithinRange()) {

            CalendarDayWithPlanDay calendarDayWithPlanDay = activityLogs.stream().filter(log -> log.date().isEqual(date)).findFirst()
                    .map(log -> calendarDayBasedOnActivityLog(date, log))
                    .orElse(calendarDayBasedOnNextDay(date, trainingPlan, currentDay));

            resultDays.add(calendarDayWithPlanDay.calendarDay);
            currentDay = calendarDayWithPlanDay.planDay;
        }

        return new Calendar(resultDays);
    }

    private CalendarDayWithPlanDay calendarDayBasedOnActivityLog(LocalDate date, ActivityLog log) {
        return new CalendarDayWithPlanDay(
                new Calendar.Day(
                        date,
                        Calendar.CalendarDayType.WORKOUT, // TODO
                        log.planDay().id().value(),
                        log.id().value()
                ),
                log.planDay()
        );
    }

    private CalendarDayWithPlanDay calendarDayBasedOnPreviousDay(LocalDate date, TrainingPlan trainingPlan, PlanDay planDay) {
        return calendarDayBasedOnPlanDay(date, trainingPlan.dayBefore(planDay));
    }

    private CalendarDayWithPlanDay calendarDayBasedOnNextDay(LocalDate date, TrainingPlan trainingPlan, PlanDay planDay) {
        return calendarDayBasedOnPlanDay(date, trainingPlan.nextDay(planDay));
    }

    private CalendarDayWithPlanDay calendarDayBasedOnPlanDay(LocalDate date, PlanDay planDay) {
        return new CalendarDayWithPlanDay(
                new Calendar.Day(
                        date,
                        Calendar.CalendarDayType.WORKOUT, //TODO :)
                        null,
                        planDay.id().value()
                ),
                planDay
        );
    }

    private boolean isActivityLogCloserThanStartDate(List<ActivityLog> activityLogs, LocalDate planStartDay, LocalDate rangeStart) {
        Optional<ActivityLog> firstLog = activityLogs.stream().min(Comparator.comparing(ActivityLog::date));
        if (firstLog.isEmpty()) {
            return false;
        }
        DateRange dateRangeFromFirstLogToRangeStart = new DateRange(firstLog.get().date(), rangeStart);
        DateRange dateRangeFromPlanStartToRangeStart = new DateRange(planStartDay, rangeStart);

        boolean isFirstLogCloserTanStartDate = dateRangeFromFirstLogToRangeStart.listDatesWithinRange().size() < dateRangeFromPlanStartToRangeStart.listDatesWithinRange().size();

        return isFirstLogCloserTanStartDate;
    }

    private record CalendarDayWithPlanDay(
            Calendar.Day calendarDay,
            PlanDay planDay) {
    }
}