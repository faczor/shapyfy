package com.shapyfy.core.boundary.api.dashboard.adapter;

import com.shapyfy.core.boundary.api.dashboard.adapter.CalendarAdapter.Calendar;
import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDayType;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.util.DateRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;

@Slf4j
@Component
public class CalendarMapper {

    public Calendar map(TrainingPlan trainingPlan, List<ActivityLog> activityLogs, DateRange dateRange) {
        log.info("Mapping training plan {} to calendar for {}", trainingPlan, dateRange);

        if (FALSE.equals(trainingPlan.isActive())) {
            return Calendar.empty(dateRange);
        }

        boolean activityLogCloserThanStartDate = isActivityLogCloserThanStartDate(activityLogs, trainingPlan.startDate(), dateRange.start());

        //TODO Consider better way of initializing strategy
        List<DayContext> dayContexts = activityLogCloserThanStartDate
                ? new CalendarActivityLogBasedMappingStrategy().map(trainingPlan, activityLogs, dateRange)
                : new CalendarTrainingPlanBasedMappingStrategy().map(trainingPlan, activityLogs, dateRange);

        return new Calendar(dayContexts.stream().map(day -> mapContextToCalendarDay(day)).toList());
    }

    private Calendar.Day mapContextToCalendarDay(DayContext day) {
        if (day.activityLog().isEmpty() && day.planDay().isEmpty()) {
            return new Calendar.Day(day.date(), Calendar.CalendarDayType.UNKNOWN, null, null);
        }

        return day.activityLog().map(activityLog -> new Calendar.Day(
                        day.date(),
                        activityLog.planDay().type() == PlanDayType.WORKOUT_DAY ? Calendar.CalendarDayType.WORKOUT : Calendar.CalendarDayType.REST,
                        activityLog.id().value(),
                        day.planDay().get().id().value()))
                .orElse(new Calendar.Day(
                        day.date(),
                        day.planDay().get().type() == PlanDayType.WORKOUT_DAY ? Calendar.CalendarDayType.WORKOUT : Calendar.CalendarDayType.REST,
                        null,
                        day.planDay().get().id().value()));

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

}