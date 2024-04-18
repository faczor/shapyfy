package com.shapyfy.core.boundary.api.dashboard;

import com.shapyfy.core.boundary.api.dashboard.adapter.CalendarAdapter;
import com.shapyfy.core.boundary.api.dashboard.adapter.CalendarAdapter.Calendar.Day;
import com.shapyfy.core.boundary.api.dashboard.adapter.CalendarMapper;
import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.model.PlanDay.PlanDayId;
import com.shapyfy.core.domain.model.PlanDayType;
import com.shapyfy.core.domain.model.Status;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.domain.model.UserId;
import com.shapyfy.core.util.DateRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.shapyfy.core.boundary.api.dashboard.adapter.CalendarAdapter.Calendar.CalendarDayType.*;
import static java.time.LocalDate.of;

public class CalendarMapperUnitTest {

    @Test
    public void shouldMapCalendar_whereFirstDayIsBeforePlanStartDate() {
        PlanDayId
                first_day_id = new PlanDayId("first_day_test_id"),
                second_day_id = new PlanDayId("second_day_test_id"),
                third_day_id = new PlanDayId("third_day_test_id");

        List<PlanDay> days = List.of(
                day(first_day_id, "PUSH", PlanDayType.WORKOUT_DAY),
                day(second_day_id, "PULL", PlanDayType.WORKOUT_DAY),
                day(third_day_id, "REST", PlanDayType.REST_DAY)
        );


        TrainingPlan plan = plan(of(2022, 1, 14), days);
        List<ActivityLog> logs = List.of();
        DateRange dateRange = new DateRange(of(2022, 1, 11), of(2022, 1, 17));

        CalendarMapper mapper = new CalendarMapper();
        CalendarAdapter.Calendar actualCalendar = mapper.map(plan, logs, dateRange);

        CalendarAdapter.Calendar expectedCalendar = new CalendarAdapter.Calendar(
                List.of(
                        new Day(of(2022, 1, 11), UNKNOWN, null, null),
                        new Day(of(2022, 1, 12), UNKNOWN, null, null),
                        new Day(of(2022, 1, 13), UNKNOWN, null, null),
                        new Day(of(2022, 1, 14), WORKOUT, null, first_day_id.value()),
                        new Day(of(2022, 1, 15), WORKOUT, null, second_day_id.value()),
                        new Day(of(2022, 1, 16), REST, null, third_day_id.value()),
                        new Day(of(2022, 1, 17), WORKOUT, null, first_day_id.value()))
        );

        Assertions.assertEquals(expectedCalendar, actualCalendar);
    }

    @Test
    public void shouldMapCalendar_whereFirstDayIsAfterPlanStartDate() {
        LocalDate plan_startDate = of(2022, 1, 14);

        PlanDayId
                first_day_id = new PlanDayId("first_day_test_id"),
                second_day_id = new PlanDayId("second_day_test_id"),
                third_day_id = new PlanDayId("third_day_test_id");

        List<PlanDay> days = List.of(
                day(first_day_id, "PUSH", PlanDayType.WORKOUT_DAY),
                day(second_day_id, "PULL", PlanDayType.WORKOUT_DAY),
                day(third_day_id, "REST", PlanDayType.REST_DAY)
        );


        TrainingPlan plan = plan(plan_startDate, days);
        List<ActivityLog> logs = List.of();
        DateRange dateRange = new DateRange(of(2022, 1, 15), of(2022, 1, 19));

        CalendarMapper mapper = new CalendarMapper();
        CalendarAdapter.Calendar actualCalendar = mapper.map(plan, logs, dateRange);

        CalendarAdapter.Calendar expectedCalendar = new CalendarAdapter.Calendar(
                List.of(
                        new Day(of(2022, 1, 15), WORKOUT, null, second_day_id.value()),
                        new Day(of(2022, 1, 16), REST, null, third_day_id.value()),
                        new Day(of(2022, 1, 17), WORKOUT, null, first_day_id.value()),
                        new Day(of(2022, 1, 18), WORKOUT, null, second_day_id.value()),
                        new Day(of(2022, 1, 19), REST, null, third_day_id.value())
                )
        );

        Assertions.assertEquals(expectedCalendar, actualCalendar);
    }

    @Test
    public void shouldMapCalendar_whereFirstDayIsSameAsPlanStartDate() {
        LocalDate plan_startDate = of(2022, 1, 14);

        PlanDayId
                first_day_id = new PlanDayId("first_day_test_id"),
                second_day_id = new PlanDayId("second_day_test_id"),
                third_day_id = new PlanDayId("third_day_test_id");

        List<PlanDay> days = List.of(
                day(first_day_id, "PUSH", PlanDayType.WORKOUT_DAY),
                day(second_day_id, "PULL", PlanDayType.WORKOUT_DAY),
                day(third_day_id, "REST", PlanDayType.REST_DAY)
        );

        CalendarMapper mapper = new CalendarMapper();
        TrainingPlan plan = plan(plan_startDate, days);
        List<ActivityLog> logs = List.of();
        DateRange dateRange = new DateRange(of(2022, 1, 14), of(2022, 1, 18));
        CalendarAdapter.Calendar actualCalendar = mapper.map(plan, logs, dateRange);

        CalendarAdapter.Calendar expectedCalendar = new CalendarAdapter.Calendar(
                List.of(
                        new Day(of(2022, 1, 14), WORKOUT, null, first_day_id.value()),
                        new Day(of(2022, 1, 15), WORKOUT, null, second_day_id.value()),
                        new Day(of(2022, 1, 16), REST, null, third_day_id.value()),
                        new Day(of(2022, 1, 17), WORKOUT, null, first_day_id.value()),
                        new Day(of(2022, 1, 18), WORKOUT, null, second_day_id.value())
                )
        );

        Assertions.assertEquals(expectedCalendar, actualCalendar);
    }

    @Test
    public void shouldMapCalendar_whereFirstDayIsBeforeFirstActivityLog() {
        LocalDate plan_startDate = of(2022, 1, 14);

        PlanDayId
                first_day_id = new PlanDayId("first_day_test_id"),
                second_day_id = new PlanDayId("second_day_test_id"),
                third_day_id = new PlanDayId("third_day_test_id");

        List<PlanDay> days = List.of(
                day(first_day_id, "PUSH", PlanDayType.WORKOUT_DAY),
                day(second_day_id, "PULL", PlanDayType.WORKOUT_DAY),
                day(third_day_id, "REST", PlanDayType.REST_DAY)
        );

        TrainingPlan plan = plan(plan_startDate, days);
        List<ActivityLog> logs = List.of(
                log(18, days.get(0)),
                log(19, days.get(1))
        );
        DateRange dateRange = new DateRange(of(2022, 1, 17), of(2022, 1, 21));

        CalendarMapper mapper = new CalendarMapper();
        CalendarAdapter.Calendar actualCalendar = mapper.map(plan, logs, dateRange);

        CalendarAdapter.Calendar expectedCalendar = new CalendarAdapter.Calendar(
                List.of(
                        new Day(of(2022, 1, 17), REST, null, third_day_id.value()),
                        new Day(of(2022, 1, 18), WORKOUT, logs.get(0).id().value(), first_day_id.value()),
                        new Day(of(2022, 1, 19), WORKOUT, logs.get(1).id().value(), second_day_id.value()),
                        new Day(of(2022, 1, 20), REST, null, third_day_id.value()),
                        new Day(of(2022, 1, 21), WORKOUT, null, first_day_id.value())
                )
        );

        Assertions.assertEquals(expectedCalendar, actualCalendar);
    }

    @Test
    public void shouldMapCalendar_whereFirstDayIsAfterFirstActivityLog() {
        LocalDate plan_startDate = of(2022, 1, 14);

        PlanDayId
                first_day_id = new PlanDayId("first_day_test_id"),
                second_day_id = new PlanDayId("second_day_test_id"),
                third_day_id = new PlanDayId("third_day_test_id");

        List<PlanDay> days = List.of(
                day(first_day_id, "PUSH", PlanDayType.WORKOUT_DAY),
                day(second_day_id, "PULL", PlanDayType.WORKOUT_DAY),
                day(third_day_id, "REST", PlanDayType.REST_DAY)
        );

        TrainingPlan plan = plan(plan_startDate, days);
        List<ActivityLog> logs = List.of(
                log(18, days.get(0)),
                log(19, days.get(1))
        );
        DateRange dateRange = new DateRange(of(2022, 1, 20), of(2022, 1, 21));

        CalendarMapper mapper = new CalendarMapper();
        CalendarAdapter.Calendar actualCalendar = mapper.map(plan, logs, dateRange);

        CalendarAdapter.Calendar expectedCalendar = new CalendarAdapter.Calendar(
                List.of(
                        new Day(of(2022, 1, 20), REST, null, third_day_id.value()),
                        new Day(of(2022, 1, 21), WORKOUT, null, first_day_id.value())
                )
        );

    }

    @Test
    public void shouldMapCalendar_whereFirstDayIsSameAsFirstActivityLog() {
        LocalDate plan_startDate = of(2022, 1, 14);

        PlanDayId
                first_day_id = new PlanDayId("first_day_test_id"),
                second_day_id = new PlanDayId("second_day_test_id"),
                third_day_id = new PlanDayId("third_day_test_id");

        List<PlanDay> days = List.of(
                day(first_day_id, "PUSH", PlanDayType.WORKOUT_DAY),
                day(second_day_id, "PULL", PlanDayType.WORKOUT_DAY),
                day(third_day_id, "REST", PlanDayType.REST_DAY)
        );

        TrainingPlan plan = plan(plan_startDate, days);
        List<ActivityLog> logs = List.of(
                log(18, days.get(0)),
                log(19, days.get(1))
        );
        DateRange dateRange = new DateRange(of(2022, 1, 18), of(2022, 1, 21));

        CalendarMapper mapper = new CalendarMapper();
        CalendarAdapter.Calendar actualCalendar = mapper.map(plan, logs, dateRange);

        CalendarAdapter.Calendar expectedCalendar = new CalendarAdapter.Calendar(
                List.of(
                        new Day(of(2022, 1, 18), WORKOUT, logs.get(0).id().value(), first_day_id.value()),
                        new Day(of(2022, 1, 19), WORKOUT, logs.get(1).id().value(), second_day_id.value()),
                        new Day(of(2022, 1, 20), REST, null, third_day_id.value()),
                        new Day(of(2022, 1, 21), WORKOUT, null, first_day_id.value())
                )
        );

        Assertions.assertEquals(expectedCalendar, actualCalendar);
    }


    private TrainingPlan plan(LocalDate startDate, List<PlanDay> days) {
        return new TrainingPlan(
                new TrainingPlan.TrainingPlanId("123"),
                "Plan name",
                Status.ACTIVE,
                days,
                startDate,
                new UserId("123")
        );
    }

    private PlanDay day(PlanDayId id, String name, PlanDayType type) {
        return new PlanDay(
                id,
                name,
                type,
                List.of()
        );
    }

    private static ActivityLog log(int dayOfMonth, PlanDay day) {
        return new ActivityLog(ActivityLog.ActivityLogId.newVal(), of(2022, 1, dayOfMonth), ActivityLog.TrackType.WORKOUT, day, List.of());
    }

}
