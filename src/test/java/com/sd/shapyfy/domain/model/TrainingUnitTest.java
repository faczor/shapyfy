package com.sd.shapyfy.domain.model;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.sd.shapyfy.domain.model.SessionState.ACTIVE;
import static com.sd.shapyfy.domain.model.SessionState.DRAFT;
import static com.sd.shapyfy.domain.model.TrainingDayType.OFF;
import static com.sd.shapyfy.domain.model.TrainingDayType.TRAINING;
import static java.time.DayOfWeek.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrainingUnitTest {

    @Test
    @DisplayName("[Training.isTrainingActive()]::Should return true on active training")
    public void shouldReturnTrue_onActiveTraining() {
        Training training_with_active_session = training_with_sessions(ACTIVE);

        assertThat(training_with_active_session.isActive()).isTrue();
    }

    @Test
    @DisplayName("[Training.isTrainingActive()]::Should return false on not active training")
    public void shouldReturnFalse_onNotActiveTraining() {
        Training training_with_active_session = training_with_sessions(DRAFT);

        assertThat(training_with_active_session.isActive()).isFalse();
    }

    @Test
    @DisplayName("[Training.isTrainingActive()]::Should return false when no session created")
    public void shouldReturnFalse_whenNoSessionCreated() {
        Training training_with_active_session = training_without_sessions();

        assertThat(training_with_active_session.isActive()).isFalse();
    }

    @Test
    @DisplayName("[Training.isDraft()]::Should return true on draft training")
    public void shouldReturnTrue_onDraftTraining() {
        Training training_with_active_session = training_with_sessions(DRAFT);

        assertThat(training_with_active_session.isDraft()).isTrue();
    }

    @Test
    @DisplayName("[Training.isDraft()]::Should return false on not draft training")
    public void shouldReturnFalse_onNotDraftTraining() {
        Training training_with_active_session = training_with_sessions(ACTIVE);

        assertThat(training_with_active_session.isDraft()).isFalse();
    }

    @Test
    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 0 when first day is training day")
    public void shouldReturn0_whenFirstDayIsTrainingDay() {
        Training training_with_first_day_training = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, OFF)));

        assertThat(training_with_first_day_training.restDaysBeforeTraining()).isEqualTo(0);
    }

    @Test
    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 2 when third day is training day")
    public void shouldReturn2_whenThirdDayIsTrainingDay() {
        Training training_with_third_day_training = training_with_days(List.of(Pair.of(MONDAY, OFF), Pair.of(TUESDAY, OFF), Pair.of(TUESDAY, TRAINING)));

        assertThat(training_with_third_day_training.restDaysBeforeTraining()).isEqualTo(2);
    }

    @Test
    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 0 when first day is training day")
    public void shouldReturn0_whenLastDayIsRestDay() {
        Training training_with_last_day_training = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, TRAINING)));

        assertThat(training_with_last_day_training.restDaysAfterTraining()).isEqualTo(0);
    }

    @Test
    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 3 when third day is training day")
    public void shouldReturn3_whenThirdDayIsTrainingDay() {
        Training training_with_last_day_training = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, OFF), Pair.of(WEDNESDAY, OFF), Pair.of(THURSDAY, OFF)));

        assertThat(training_with_last_day_training.restDaysAfterTraining()).isEqualTo(3);
    }

    @Test
    @DisplayName("[Training.firstTrainingDay()]::Should return first day when first day is training day")
    public void shouldReturnFirstDay_whenFirstDayIsTrainingDay() {
        Training training_with_last_day_training = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, OFF)));

        assertThat(training_with_last_day_training.firstTrainingDay()).satisfies(day -> assertThat(day.getDay()).isEqualTo(MONDAY));
    }

    @Test
    @DisplayName("[Training.firstTrainingDay()]::Should return third day when third day is training day")
    public void shouldReturnThirdDay_whenThirdDayIsTrainingDay() {
        Training training_with_last_day_training = training_with_days(List.of(Pair.of(MONDAY, OFF), Pair.of(TUESDAY, OFF), Pair.of(WEDNESDAY, OFF), Pair.of(THURSDAY, TRAINING)));

        assertThat(training_with_last_day_training.firstTrainingDay()).satisfies(day -> assertThat(day.getDay()).isEqualTo(THURSDAY));
    }

    @Test
    @DisplayName("[Training.firstTrainingDay()]::Should throw when no training day found")
    public void shouldThrowException_whenNoTrainingDayFound() {
        Training training_without_days = training_with_days(List.of());

        assertThrows(IllegalStateException.class, training_without_days::restDaysBeforeTraining);
    }

    @Test
    @DisplayName("[Training.lastTrainingDay()]::Should return last day when last day is training day")
    public void shouldReturnLastDay_whenLastDayIsTrainingDay() {
        Training training_with_last_day_training = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, OFF), Pair.of(WEDNESDAY, OFF), Pair.of(THURSDAY, TRAINING), Pair.of(FRIDAY, OFF)));

        assertThat(training_with_last_day_training.lastTrainingDay()).satisfies(day -> assertThat(day.getDay()).isEqualTo(THURSDAY));
    }


    private Training training_with_sessions(SessionState state) {
        return new Training(
                PlanId.of(UUID.randomUUID()),
                UserId.of("user-id-00001"),
                "Name",
                List.of(training_day(state, MONDAY))
        );
    }

    private Training training_with_days(List<Pair<DayOfWeek, TrainingDayType>> trainingDayParams) {
        return new Training(
                PlanId.of(UUID.randomUUID()),
                UserId.of("user-id-00001"),
                "Name",
                trainingDayParams.stream().map(param -> training_day(param.getValue(), param.getKey())).toList()
        );
    }

    private Training training_without_sessions() {
        return new Training(
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

    private TrainingDay training_day(TrainingDayType type, DayOfWeek dayOfWeek) {
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
