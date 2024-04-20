package com.shapyfy.core.boundary.api.dashboard.adapter;

import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.util.DateRange;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;

public class CalendarTrainingPlanBasedMappingStrategy extends CalendarMappingStrategy {

    @Override
    List<DayContext> map(TrainingPlan trainingPlan, List<ActivityLog> activityLogs, DateRange dateRange) {

        List<LocalDate> datesAfterStart = dateRange.streamDatesWithinRange().filter(date -> date.isAfter(trainingPlan.getStartDate())).toList();
        PlanDay firstDay = trainingPlan.getDays().getFirst();

        return Stream.of(
                        dateRange.streamDatesWithinRange().filter(date -> date.isBefore(trainingPlan.getStartDate())).map(DayContext::empty),
                        dateRange.streamDatesWithinRange().filter(date -> date.equals(trainingPlan.getStartDate())).map(date -> DayContext.day(date, firstDay)),
                        Optional.of(datesAfterStart).filter(Predicate.not(List::isEmpty))
                                .map(dates -> futureDayContexts(new DateRange(dates.getFirst(), dates.getLast()), trainingPlan, activityLogs, firstDay).stream()).orElse(Stream.empty())
                )
                .flatMap(identity())
                .sorted(comparing(DayContext::date)).toList();
    }
}
