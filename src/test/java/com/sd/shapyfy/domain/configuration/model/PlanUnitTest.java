package com.sd.shapyfy.domain.configuration.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlanUnitTest {

//    @Test
//    @DisplayName("[Training.isTrainingActive()]::Should return true on active training")
//    public void shouldReturnTrue_onActiveTraining() {
//        PlanConfiguration plan_with_active_session = training_with_sessions(ACTIVE);
//
//        assertThat(plan_with_active_session.isActive()).isTrue();
//    }
//
//    @Test
//    @DisplayName("[Training.isTrainingActive()]::Should return false on not active training")
//    public void shouldReturnFalse_onNotActiveTraining() {
//        PlanConfiguration plan_with_active_session = training_with_sessions(DRAFT);
//
//        assertThat(plan_with_active_session.isActive()).isFalse();
//    }
//
//    @Test
//    @DisplayName("[Training.isTrainingActive()]::Should return false when no session created")
//    public void shouldReturnFalse_whenNoSessionCreated() {
//        PlanConfiguration plan_with_active_session = training_without_sessions();
//
//        assertThat(plan_with_active_session.isActive()).isFalse();
//    }
//
//    @Test
//    @DisplayName("[Training.isDraft()]::Should return true on draft training")
//    public void shouldReturnTrue_onDraftTraining() {
//        PlanConfiguration plan_with_active_session = training_with_sessions(DRAFT);
//
//        assertThat(plan_with_active_session.isDraft()).isTrue();
//    }
//
//    @Test
//    @DisplayName("[Training.isDraft()]::Should return false on not draft training")
//    public void shouldReturnFalse_onNotDraftTraining() {
//        PlanConfiguration plan_with_active_session = training_with_sessions(ACTIVE);
//
//        assertThat(plan_with_active_session.isDraft()).isFalse();
//    }
//
//    @Test
//    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 0 when first day is training day")
//    public void shouldReturn0_whenFirstDayIsTrainingDay() {
//        PlanConfiguration training_with_first_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, REST)));
//
//        assertThat(training_with_first_day_plan.restDaysBeforeTraining()).isEqualTo(0);
//    }
//
//    @Test
//    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 2 when third day is training day")
//    public void shouldReturn2_whenThirdDayIsTrainingDay() {
//        PlanConfiguration training_with_third_day_plan = training_with_days(List.of(Pair.of(MONDAY, REST), Pair.of(TUESDAY, REST), Pair.of(TUESDAY, TRAINING)));
//
//        assertThat(training_with_third_day_plan.restDaysBeforeTraining()).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 0 when first day is training day")
//    public void shouldReturn0_whenLastDayIsRestDay() {
//        PlanConfiguration training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, TRAINING)));
//
//        assertThat(training_with_last_day_plan.restDaysAfterTraining()).isEqualTo(0);
//    }
//
//    @Test
//    @DisplayName("[Training.restDaysBeforeTraining()]::Should return 3 when third day is training day")
//    public void shouldReturn3_whenThirdDayIsTrainingDay() {
//        PlanConfiguration training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, REST), Pair.of(WEDNESDAY, REST), Pair.of(THURSDAY, REST)));
//
//        assertThat(training_with_last_day_plan.restDaysAfterTraining()).isEqualTo(3);
//    }
//
//    @Test
//    @DisplayName("[Training.firstTrainingDay()]::Should return first day when first day is training day")
//    public void shouldReturnFirstDay_whenFirstDayIsTrainingDay() {
//        PlanConfiguration training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, REST)));
//
//        assertThat(training_with_last_day_plan.firstTrainingDay()).satisfies(day -> assertThat(day.getDay()).isEqualTo(MONDAY));
//    }
//
//    @Test
//    @DisplayName("[Training.firstTrainingDay()]::Should return third day when third day is training day")
//    public void shouldReturnThirdDay_whenThirdDayIsTrainingDay() {
//        PlanConfiguration training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, REST), Pair.of(TUESDAY, REST), Pair.of(WEDNESDAY, REST), Pair.of(THURSDAY, TRAINING)));
//
//        assertThat(training_with_last_day_plan.firstTrainingDay()).satisfies(day -> assertThat(day.getDay()).isEqualTo(THURSDAY));
//    }
//
//    @Test
//    @DisplayName("[Training.firstTrainingDay()]::Should throw when no training day found")
//    public void shouldThrowException_whenNoTrainingDayFound() {
//        PlanConfiguration plan_without_days = training_with_days(List.of());
//
//        assertThrows(IllegalStateException.class, plan_without_days::restDaysBeforeTraining);
//    }
//
//    @Test
//    @DisplayName("[Training.lastTrainingDay()]::Should return last day when last day is training day")
//    public void shouldReturnLastDay_whenLastDayIsTrainingDay() {
//        PlanConfiguration training_with_last_day_plan = training_with_days(List.of(Pair.of(MONDAY, TRAINING), Pair.of(TUESDAY, REST), Pair.of(WEDNESDAY, REST), Pair.of(THURSDAY, TRAINING), Pair.of(FRIDAY, REST)));
//
//        assertThat(training_with_last_day_plan.lastTrainingDay()).satisfies(day -> assertThat(day.getDay()).isEqualTo(THURSDAY));
//    }
//
}
