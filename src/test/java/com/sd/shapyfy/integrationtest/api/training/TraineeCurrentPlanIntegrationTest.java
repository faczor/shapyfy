package com.sd.shapyfy.integrationtest.api.training;

import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

public class TraineeCurrentPlanIntegrationTest extends AbstractIntegrationTest {

//    @Autowired
//    private PostgresSessionRepository sessionRepository;
//
//    @Autowired
//    private PostgresSessionExerciseRepository sessionExerciseRepository;
//
//    @Test
//    @DisplayName("Trainnee display current training EP[POST:/v1/current-trainings]")
//    void fetchCurrentTraining() {
//        as(USER_WITH_DRAFT_TRAINING.getTestUser()).assertRequest($ -> $.get("/v1/current-trainings"))
//                //
//                .statusCode(200)
//                //
//                .body("id", is("00000000-0000-0000-0000-000000000200"))
//                .body("name", is("PUSH A,PULL A,LEGS A,null,PUSH B,PULL B,LEGS B,null"))
//                //
//                .body("training_days[0].id", is("00000000-0000-0000-0000-000000000201"))
//                .body("training_days[0].name", is("PUSH A"))
//                .body("training_days[0].day_type", is("TRAINING"))
//                .body("training_days[0].day_of_week", is("MONDAY"))
//                //
//                .body("training_days[0].training_exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
//                .body("training_days[0].training_exercises[0].name", is("TEST_EXERCISE_1"))
//                .body("training_days[0].training_exercises[0].sets", is(3))
//                .body("training_days[0].training_exercises[0].reps", is(8))
//                .body("training_days[0].training_exercises[0].weight", is(25.5f))
//                //
//                .body("training_days[0].training_exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000003"))
//                .body("training_days[0].training_exercises[1].name", is("TEST_EXERCISE_3"))
//                .body("training_days[0].training_exercises[1].sets", is(4))
//                .body("training_days[0].training_exercises[1].reps", is(6))
//                .body("training_days[0].training_exercises[1].weight", is(80.0f))
//                //
//                .body("training_days[0].training_exercises[2].exercise_id", is("00000000-0000-0000-0000-000000000005"))
//                .body("training_days[0].training_exercises[2].name", is("TEST_EXERCISE_5"))
//                .body("training_days[0].training_exercises[2].sets", is(3))
//                .body("training_days[0].training_exercises[2].reps", is(8))
//                .body("training_days[0].training_exercises[2].weight", nullValue())
//                //
//                //
//                .body("training_days[1].id", is("00000000-0000-0000-0000-000000000202"))
//                .body("training_days[1].name", is("PULL A"))
//                .body("training_days[1].day_type", is("TRAINING"))
//                .body("training_days[1].day_of_week", is("TUESDAY"))
//                //
//                .body("training_days[1].training_exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
//                .body("training_days[1].training_exercises[0].name", is("TEST_EXERCISE_1"))
//                .body("training_days[1].training_exercises[0].sets", is(3))
//                .body("training_days[1].training_exercises[0].reps", is(12))
//                .body("training_days[1].training_exercises[0].weight", is(10.0f))
//                //
//                .body("training_days[1].training_exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000002"))
//                .body("training_days[1].training_exercises[1].name", is("TEST_EXERCISE_2"))
//                .body("training_days[1].training_exercises[1].sets", is(3))
//                .body("training_days[1].training_exercises[1].reps", is(10))
//                .body("training_days[1].training_exercises[1].weight", is(30.0f))
//                //
//                .body("training_days[1].training_exercises[2].exercise_id", is("00000000-0000-0000-0000-000000000003"))
//                .body("training_days[1].training_exercises[2].name", is("TEST_EXERCISE_3"))
//                .body("training_days[1].training_exercises[2].sets", is(1))
//                .body("training_days[1].training_exercises[2].reps", is(8))
//                .body("training_days[1].training_exercises[2].weight", nullValue())
//                //
//                //
//                .body("training_days[2].id", is("00000000-0000-0000-0000-000000000203"))
//                .body("training_days[2].name", is("LEGS A"))
//                .body("training_days[2].day_type", is("TRAINING"))
//                .body("training_days[2].day_of_week", is("WEDNESDAY"))
//                //
//                .body("training_days[2].training_exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000004"))
//                .body("training_days[2].training_exercises[0].name", is("TEST_EXERCISE_4"))
//                .body("training_days[2].training_exercises[0].sets", is(3))
//                .body("training_days[2].training_exercises[0].reps", is(8))
//                .body("training_days[2].training_exercises[0].weight", is(25.5f))
//                //
//                //
//                .body("training_days[3].id", is("00000000-0000-0000-0000-000000000204"))
//                .body("training_days[3].name", is("REST_DAY"))
//                .body("training_days[3].day_type", is("REST"))
//                .body("training_days[3].day_of_week", is("THURSDAY"))
//                .body("training_days[3].training_exercises", is(emptyIterable()))
//        ;
//    }
//
//    @Test
//    @DisplayName("Trainnee display current training without trainings EP[POST:/v1/current-trainings]")
//    void currentTrainingForNewUser() {
//        as(NEW_USER.getTestUser()).assertRequest($ -> $.get("/v1/current-trainings"))
//                //
//                .statusCode(400)
//                .body(is("Current training not found for UserId::00000000000000000"));
//
//    }
//
//    @Test
//    @DisplayName("Trainee run todays session EP[PATCH:/v1/current-trainings]")
//    void startTodaySession() {
//        String sessionId = as(USER_WITH_ACTIVE_TRAINING.getTestUser()).assertRequest($ -> $
//                        .patch("/v1/current-trainings"))
//                //
//                .statusCode(200)
//                .body("session_id", is("00000000-0000-0000-0000-000000030101"))
//                //
//                .body("training_exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
//                .body("training_exercises[0].name", is("TEST_EXERCISE_1"))
//                .body("training_exercises[0].sets", is(3))
//                .body("training_exercises[0].reps", is(8))
//                .body("training_exercises[0].weight", is(25.5f))
//                //
//                .body("training_exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000003"))
//                .body("training_exercises[1].name", is("TEST_EXERCISE_3"))
//                .body("training_exercises[1].sets", is(4))
//                .body("training_exercises[1].reps", is(6))
//                .body("training_exercises[1].weight", is(80.0f))
//                //
//                .body("training_exercises[2].exercise_id", is("00000000-0000-0000-0000-000000000005"))
//                .body("training_exercises[2].name", is("TEST_EXERCISE_5"))
//                .body("training_exercises[2].sets", is(3))
//                .body("training_exercises[2].reps", is(8))
//                .body("training_exercises[2].weight", nullValue())
//                //
//                .and().extract().body().path("session_id");
//
//        assertThat(sessionRepository.findById(UUID.fromString(sessionId)))
//                .isPresent()
//                .get()
//                .satisfies(session -> assertThat(session.getState()).isEqualTo(RUNNING));
//    }
//
//    @Test
//    @DisplayName("Trainee finish exercise EP[PATCH:/v1/current-trainings/{exercise_id}/finish-exercises]")
//    void finishTraining() {
//        UUID exerciseId = UUID.fromString("00000000-0000-0000-0005-000004010103");
//        as(USER_WITH_RUNNING_TRAINING.getTestUser()).assertRequest($ -> $
//                        .body("""
//                                {
//                                  "weight": "49.5"
//                                }
//                                """)
//                .patch("/v1/current-trainings/{exercise_id}/finish-exercises", exerciseId))
//                //
//                .statusCode(200);
//
//        assertThat(sessionExerciseRepository.findById(exerciseId))
//                .isPresent()
//                .get()
//                .satisfies(session -> {
//                    assertThat(session.isFinished()).isTrue();
//                    assertThat(session.getWeightAmount()).isEqualTo(49.5);
//                });
//    }
//
//    @Test
//    @DisplayName("Trainee finish session EP[PATCH:/v1/current-trainings/{session_id}/finish-session]")
//    void finishSession() {
//        UUID sessionId = UUID.fromString("00000000-0000-0000-0000-000000050101");
//        as(USER_WITH_RUNNING_TRAINING_READY_TO_FINISH.getTestUser()).assertRequest($ -> $
//                .patch("/v1/current-trainings/{session_id}/finish-sessions", sessionId))
//                //
//                .statusCode(200);
//
//        assertThat(sessionRepository.findById(sessionId))
//                .isPresent()
//                .get()
//                .satisfies(session -> {
//                    assertThat(session.getState()).isEqualTo(FINISHED);
//                });
//    }
}
