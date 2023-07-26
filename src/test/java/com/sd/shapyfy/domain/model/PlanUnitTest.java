package com.sd.shapyfy.domain.model;

import com.sd.shapyfy.domain.plan.PlanId;
import com.sd.shapyfy.domain.session.Session;
import com.sd.shapyfy.domain.session.SessionId;
import com.sd.shapyfy.domain.session.SessionState;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.sd.shapyfy.domain.session.SessionState.ACTIVE;
import static com.sd.shapyfy.domain.session.SessionState.DRAFT;
import static com.sd.shapyfy.domain.model.ConfigurationDayType.REST;
import static com.sd.shapyfy.domain.model.ConfigurationDayType.TRAINING;
import static java.time.DayOfWeek.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlanUnitTest {

    @Test
    @DisplayName("[Training.isTrainingActive()]::Should return true on active training")
    public void shouldReturnTrue_onActiveTraining() {
        Plan plan_with_active_session = training_with_sessions(ACTIVE);

        assertThat(plan_with_active_session.isActive()).isTrue();
    }

    @Test
    @DisplayName("[Training.isTrainingActive()]::Should return false on not active training")
    public void shouldReturnFalse_onNotActiveTraining() {
        Plan plan_with_active_session = training_with_sessions(DRAFT);

        assertThat(plan_with_active_session.isActive()).isFalse();
    }

    @Test
    @DisplayName("[Training.isTrainingActive()]::Should return false when no session created")
    public void shouldReturnFalse_whenNoSessionCreated() {
        Plan plan_with_active_session = training_without_sessions();

        assertThat(plan_with_active_session.isActive()).isFalse();
    }

    @Test
    @DisplayName("[Training.isDraft()]::Should return true on draft training")
    public void shouldReturnTrue_onDraftTraining() {
        Plan plan_with_active_session = training_with_sessions(DRAFT);

        assertThat(plan_with_active_session.isDraft()).isTrue();
    }

    @Test
    @DisplayName("[Training.isDraft()]::Should return false on not draft training")
    public void shouldReturnFalse_onNotDraftTraining() {
        Plan plan_with_active_session = training_with_sessions(ACTIVE);

        assertThat(plan_with_active_session.isDraft()).isFalse();
    }

    @Test
    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 0 when first day is training day")
    public void shouldReturn0_whenFirstDayIsTrainingDay() {
        Plan training_with_first_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, REST)));

        assertThat(training_with_first_day_plan.restDaysBeforeTraining()).isEqualTo(0);
    }

    @Test
    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 2 when third day is training day")
    public void shouldReturn2_whenThirdDayIsTrainingDay() {
        Plan training_with_third_day_plan = training_with_days(List.of(Pair.of(MONDAY, REST), Pair.of(TUESDAY, REST), Pair.of(TUESDAY, TRAINING)));

        assertThat(training_with_third_day_plan.restDaysBeforeTraining()).isEqualTo(2);
    }

    @Test
    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 0 when first day is training day")
    public void shouldReturn0_whenLastDayIsRestDay() {
        Plan training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, TRAINING)));

        assertThat(training_with_last_day_plan.restDaysAfterTraining()).isEqualTo(0);
    }

    @Test
    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 3 when third day is training day")
    public void shouldReturn3_whenThirdDayIsTrainingDay() {
        Plan training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, REST), Pair.of(WEDNESDAY, REST), Pair.of(THURSDAY, REST)));

        assertThat(training_with_last_day_plan.restDaysAfterTraining()).isEqualTo(3);
    }

    @Test
    @DisplayName("[Training.firstTrainingDay()]::Should return first day when first day is training day")
    public void shouldReturnFirstDay_whenFirstDayIsTrainingDay() {
        Plan training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, REST)));

        assertThat(training_with_last_day_plan.firstTrainingDay()).satisfies(day -> assertThat(day.getDay()).isEqualTo(MONDAY));
    }

    @Test
    @DisplayName("[Training.firstTrainingDay()]::Should return third day when third day is training day")
    public void shouldReturnThirdDay_whenThirdDayIsTrainingDay() {
        Plan training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, REST), Pair.of(TUESDAY, REST), Pair.of(WEDNESDAY, REST), Pair.of(THURSDAY, TRAINING)));

        assertThat(training_with_last_day_plan.firstTrainingDay()).satisfies(day -> assertThat(day.getDay()).isEqualTo(THURSDAY));
    }

    @Test
    @DisplayName("[Training.firstTrainingDay()]::Should throw when no training day found")
    public void shouldThrowException_whenNoTrainingDayFound() {
        Plan plan_without_days = training_with_days(List.of());

        assertThrows(IllegalStateException.class, plan_without_days::restDaysBeforeTraining);
    }

    @Test
    @DisplayName("[Training.lastTrainingDay()]::Should return last day when last day is training day")
    public void shouldReturnLastDay_whenLastDayIsTrainingDay() {
        Plan training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, REST), Pair.of(WEDNESDAY, REST), Pair.of(THURSDAY, TRAINING), Pair.of(FRIDAY, REST)));

        assertThat(training_with_last_day_plan.lastTrainingDay()).satisfies(day -> assertThat(day.getDay()).isEqualTo(THURSDAY));
    }


    private Plan training_with_sessions(SessionState state) {
        return new Plan(
                PlanId.of(UUID.randomUUID()),
                UserId.of("user-id-00001"),
                "Name",
                List.of(training_day(state, MONDAY))
        );
    }

    private Plan training_with_days(List<Pair<DayOfWeek, ConfigurationDayType>> trainingDayParams) {
        return new Plan(
                PlanId.of(UUID.randomUUID()),
                UserId.of("user-id-00001"),
                "Name",
                trainingDayParams.stream().map(param -> training_day(param.getValue(), param.getKey())).toList()
        );
    }

    private Plan training_without_sessions() {
        return new Plan(
                PlanId.of(UUID.randomUUID()),
                UserId.of("user-id-00001"),
                "Name",
                List.of(training_day_without_sessions())
        );
    }

    private TrainingDay training_day_without_sessions() {
        return new TrainingDay(
                TrainingDayId.of(UUID.randomUUID()),
                "Name",
                MONDAY,
                TRAINING,
                List.of()
        );
    }

    private TrainingDay training_day(SessionState state, DayOfWeek dayOfWeek) {
        return new TrainingDay(
                TrainingDayId.of(UUID.randomUUID()),
                "Name",
                dayOfWeek,
                TRAINING,
                List.of(session(state))
        );
    }

    private TrainingDay training_day(ConfigurationDayType type, DayOfWeek dayOfWeek) {
        return new TrainingDay(
                TrainingDayId.of(UUID.randomUUID()),
                "Name",
                dayOfWeek,
                type,
                List.of(session(ACTIVE))
        );
    }

    private Session session(SessionState state) {
        return new Session(
                SessionId.of(UUID.randomUUID()),
                state,
                LocalDate.now(),
                List.of());
    }

}
