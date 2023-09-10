package com.sd.shapyfy.domain.plan.model;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainingUnitTest {

//   @Test
//   @DisplayName("[Training.stateFor()]::Return training day for existing session on date")
//   void shouldReturnTrainingDay() {
//       ConfigurationDay
//               training_day_1 = c_day(TRAINING),
//               training_day_2 = c_day(TRAINING),
//               rest_day = c_day(REST);
//       SessionPart
//               session_Part_for_day_1 = s_part(training_day_1.id(), date(1)),
//               session_Part_for_day_2 = s_part(training_day_2.id(), date(2));
//       com.sd.shapyfy.domain.plan.model.Training
//               training = training(List.of(training_day_1, training_day_2, rest_day), List.of(session_Part_for_day_1, session_Part_for_day_2), date(1), date(3));

//       assertThat(training.stateFor(date(1))).satisfies(stateForDate -> {
//           assertThat(stateForDate.date()).isEqualTo(LocalDate.of(2023, 1, 1));
//           assertThat(stateForDate.configurationDay().id()).isEqualTo(training_day_1.id());
//           assertThat(stateForDate.isTrainingDay()).isTrue();
//       });
//   }

//   @Test
//   @DisplayName("[Training.stateFor()]::Return rest day for existing session on date")
//   void shouldReturnRestDay() {
//       ConfigurationDay
//               training_day_1 = c_day(TRAINING),
//               training_day_2 = c_day(TRAINING),
//               rest_day = c_day(REST);
//       SessionPart
//               session_Part_for_day_1 = s_part(training_day_1.id(), date(1)),
//               session_Part_for_day_2 = s_part(training_day_2.id(), date(2));
//       com.sd.shapyfy.domain.plan.model.Training
//               training = training(List.of(training_day_1, training_day_2, rest_day), List.of(session_Part_for_day_1, session_Part_for_day_2), date(1), date(3));

//       assertThat(training.stateFor(date(3))).satisfies(stateForDate -> {
//           assertThat(stateForDate.date()).isEqualTo(LocalDate.of(2023, 1, 3));
//           //assertThat(stateForDate.configurationDay().id()).isEqualTo(rest_day.id());
//           assertThat(stateForDate.isTrainingDay()).isFalse();
//       });
//   }

//   @Test
//   @DisplayName("[Training.stateFor()]::Return training day for future days")
//   void shouldReturnTrainingDayForFutureState() {
//       ConfigurationDay
//               training_day_1 = c_day(TRAINING),
//               training_day_2 = c_day(TRAINING),
//               rest_day = c_day(REST);
//       SessionPart
//               session_Part_for_day_1 = s_part(training_day_1.id(), date(1)),
//               session_Part_for_day_2 = s_part(training_day_2.id(), date(2));
//       com.sd.shapyfy.domain.plan.model.Training
//               training = training(List.of(training_day_1, training_day_2, rest_day), List.of(session_Part_for_day_1, session_Part_for_day_2), date(1), date(3));

//       assertThat(training.stateFor(date(4))).satisfies(stateForDate -> {
//           assertThat(stateForDate.date()).isEqualTo(LocalDate.of(2023, 1, 4));
//           assertThat(stateForDate.configurationDay().id()).isEqualTo(training_day_1.id());
//           assertThat(stateForDate.isTrainingDay()).isTrue();
//       });
//   }

//   @Test
//   @DisplayName("[Training.stateFor()]::Return rest day for future days")
//   void shouldReturnRestDayForFutureState() {
//       ConfigurationDay
//               training_day_1 = c_day(TRAINING),
//               training_day_2 = c_day(TRAINING),
//               rest_day = c_day(REST);
//       SessionPart
//               session_Part_for_day_1 = s_part(training_day_1.id(), date(1)),
//               session_Part_for_day_2 = s_part(training_day_2.id(), date(2));
//       com.sd.shapyfy.domain.plan.model.Training
//               training = training(List.of(training_day_1, training_day_2, rest_day), List.of(session_Part_for_day_1, session_Part_for_day_2), date(1), date(3));

//       assertThat(training.stateFor(date(9))).satisfies(stateForDate -> {
//           assertThat(stateForDate.date()).isEqualTo(LocalDate.of(2023, 1, 9));
//           //assertThat(stateForDate.configurationDay().id()).isEqualTo(rest_day.id());
//           assertThat(stateForDate.isTrainingDay()).isFalse();
//       });
//   }


//   @Test
//   @DisplayName("[Training.stateFor()]::Return no configuration on date with no training set")
//   void shouldEmptyConfiguration() {
//       ConfigurationDay
//               training_day_1 = c_day(TRAINING),
//               training_day_2 = c_day(TRAINING),
//               rest_day = c_day(REST);
//       SessionPart
//               session_Part_for_day_1 = s_part(training_day_1.id(), date(3)),
//               session_Part_for_day_2 = s_part(training_day_2.id(), date(4));
//       com.sd.shapyfy.domain.plan.model.Training
//               training = training(List.of(training_day_1, training_day_2, rest_day), List.of(session_Part_for_day_1, session_Part_for_day_2), date(3), date(5));

//       assertThat(training.stateFor(date(1))).satisfies(stateForDate -> {
//           assertThat(stateForDate.date()).isEqualTo(LocalDate.of(2023, 1, 1));
//           assertThat(stateForDate.configurationDay()).isNull();
//           assertThat(stateForDate.isTrainingDay()).isFalse();
//       });
//   }

//   private com.sd.shapyfy.domain.plan.model.Training training(List<ConfigurationDay> c_days, List<SessionPart> sessionParts, LocalDate from, LocalDate to) {
//       return new com.sd.shapyfy.domain.plan.model.Training(
//               configuration(c_days),
//               List.of(new Session(SessionId.of(UUID.randomUUID()), sessionParts, new DateRange(from, to)))
//       );
//   }

//   private Training configuration(List<ConfigurationDay> c_days) {
//       return new Training(
//               null,
//               c_days
//       );
//   }

//   private ConfigurationDay c_day(ConfigurationDayType type) {
//       UUID uuid = UUID.randomUUID();
//       return new ConfigurationDay(
//               ConfigurationDayId.of(uuid),
//               type,
//               type + "::" + uuid,
//               List.of()
//       );
//   }

//   private SessionPart s_part(ConfigurationDayId configurationDayId, LocalDate date) {
//       return new SessionPart(
//               SessionPartId.of(UUID.randomUUID()),
//               configurationDayId,
//               SessionState.ACTIVE,
//               date,
//               List.of()
//       );
//   }

//   //TODO to common
//   private static LocalDate date(int day) {
//        return LocalDate.of(2023, 1, day);
//    }

}
