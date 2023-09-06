package com.sd.shapyfy.integrationtest.api.training_day;

import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.USER_WITH_ACTIVE_TRAINING;
import static org.hamcrest.Matchers.is;

class TrainingDayIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Trainee get session day EP[GET:/v1/sessions/{session_id}")
    void getTrainingDay() {

        as(USER_WITH_ACTIVE_TRAINING.getTestUser()).assertRequest($ -> $
                        .get("/v1/sessions/00000000-0000-0000-0002-000000020101/part/00000000-0000-0000-0002-000002010101"))
                .statusCode(200)
                //
                .body("id", is("00000000-0000-0000-0002-000002010101"))
                .body("name", is("PUSH"))
                .body("type", is("TRAINING_DAY"))
                .body("state", is("ACTIVE"))
                //
                .body("exercises[0].id", is("00000000-0000-0000-0002-000201010101"))
                .body("exercises[0].exercise.id", is("00000000-0000-0000-0000-000000000001"))
                .body("exercises[0].exercise.name", is("TEST_EXERCISE_1"))
                .body("exercises[0].break_between_sets.minutes", is(1))
                .body("exercises[0].break_between_sets.seconds", is(0))
                .body("exercises[0].is_finished", is(false))
                .body("exercises[0].attributes.size()", is(0))
                //
                .body("exercises[0].sets[0].id", is("00000000-0000-0000-0002-020101010101"))
                .body("exercises[0].sets[0].reps", is(8))
                .body("exercises[0].sets[0].weight", is(40.0f))
                .body("exercises[0].sets[0].is_finished", is(false))
                .body("exercises[0].sets[0].attributes.size()", is(0))
                //
                .body("exercises[0].sets[1].id", is("00000000-0000-0000-0002-020101010102"))
                .body("exercises[0].sets[1].reps", is(8))
                .body("exercises[0].sets[1].weight", is(40.0f))
                .body("exercises[0].sets[1].is_finished", is(false))
                .body("exercises[0].sets[1].attributes.size()", is(0))
                //
                .body("exercises[0].sets[2].id", is("00000000-0000-0000-0002-020101010103"))
                .body("exercises[0].sets[2].reps", is(8))
                .body("exercises[0].sets[2].weight", is(40.0f))
                .body("exercises[0].sets[2].is_finished", is(false))
                .body("exercises[0].sets[2].attributes.size()", is(0))
                //
                //
                .body("exercises[1].id", is("00000000-0000-0000-0002-000201010102"))
                .body("exercises[1].exercise.id", is("00000000-0000-0000-0000-000000000002"))
                .body("exercises[1].exercise.name", is("TEST_EXERCISE_2"))
                .body("exercises[1].break_between_sets.minutes", is(1))
                .body("exercises[1].break_between_sets.seconds", is(30))
                .body("exercises[1].is_finished", is(false))
                .body("exercises[1].attributes.size()", is(0))
                //
                .body("exercises[1].sets[0].id", is("00000000-0000-0000-0002-020101010201"))
                .body("exercises[1].sets[0].reps", is(6))
                .body("exercises[1].sets[0].weight", is(60.0f))
                .body("exercises[1].sets[0].is_finished", is(false))
                .body("exercises[1].sets[0].attributes.size()", is(0))
                //
                .body("exercises[1].sets[1].id", is("00000000-0000-0000-0002-020101010202"))
                .body("exercises[1].sets[1].reps", is(6))
                .body("exercises[1].sets[1].weight", is(60.0f))
                .body("exercises[1].sets[1].is_finished", is(false))
                .body("exercises[1].sets[1].attributes.size()", is(0))
                //
                .body("exercises[1].sets[2].id", is("00000000-0000-0000-0002-020101010203"))
                .body("exercises[1].sets[2].reps", is(6))
                .body("exercises[1].sets[2].weight", is(60.0f))
                .body("exercises[1].sets[2].is_finished", is(false))
                .body("exercises[1].sets[2].attributes.size()", is(0))
                //
                .body("exercises[1].sets[3].id", is("00000000-0000-0000-0002-020101010204"))
                .body("exercises[1].sets[3].reps", is(6))
                .body("exercises[1].sets[3].weight", is(60.0f))
                .body("exercises[1].sets[3].is_finished", is(false))
                .body("exercises[1].sets[3].attributes.size()", is(0))
                //
                //
                .body("exercises[2].id", is("00000000-0000-0000-0002-000201010103"))
                .body("exercises[2].exercise.id", is("00000000-0000-0000-0000-000000000003"))
                .body("exercises[2].exercise.name", is("TEST_EXERCISE_3"))
                .body("exercises[2].break_between_sets.minutes", is(2))
                .body("exercises[2].break_between_sets.seconds", is(0))
                .body("exercises[2].is_finished", is(false))
                .body("exercises[2].attributes.size()", is(0))
                //
                .body("exercises[2].sets[0].id", is("00000000-0000-0000-0002-020101010301"))
                .body("exercises[2].sets[0].reps", is(6))
                .body("exercises[2].sets[0].weight", is(110.5f))
                .body("exercises[2].sets[0].is_finished", is(false))
                .body("exercises[2].sets[0].attributes.size()", is(0))
                //
                .body("exercises[2].sets[1].id", is("00000000-0000-0000-0002-020101010302"))
                .body("exercises[2].sets[1].reps", is(6))
                .body("exercises[2].sets[1].weight", is(110.5f))
                .body("exercises[2].sets[1].is_finished", is(false))
                .body("exercises[2].sets[1].attributes.size()", is(0))
                //
                .body("exercises[2].sets[2].id", is("00000000-0000-0000-0002-020101010303"))
                .body("exercises[2].sets[2].reps", is(6))
                .body("exercises[2].sets[2].weight", is(110.5f))
                .body("exercises[2].sets[2].is_finished", is(false))
                .body("exercises[2].sets[2].attributes.size()", is(0))
                //
                .body("exercises[2].sets[3].id", is("00000000-0000-0000-0002-020101010304"))
                .body("exercises[2].sets[3].reps", is(6))
                .body("exercises[2].sets[3].weight", is(110.5f))
                .body("exercises[2].sets[3].is_finished", is(false))
                .body("exercises[2].sets[3].attributes.size()", is(0))
        ;
    }

    //TODO prepare proper data and test it
    @Test
    @DisplayName("Trainee run exercise on training day EP[GET:/v1/sessions/{session_id}/part/{session_part_id}/exercise/{exercise_id}/starts")
    void runTrainingDayExercise() {
        as(USER_WITH_ACTIVE_TRAINING.getTestUser()).assertRequest($ -> $
                        .patch("/v1/sessions/{session_id}/part/{session_part_id}/exercise/{exercise_id}/starts",
                                "00000000-0000-0000-0002-000000020101",
                                "00000000-0000-0000-0002-000002010101",
                                "00000000-0000-0000-0000-000000000001"))
                //
                .statusCode(200)
                .body("id", is("00000000-0000-0000-0002-000201010101"))
                .body("exercise.id", is("00000000-0000-0000-0000-000000000001"))
                .body("exercise.name", is("TEST_EXERCISE_1"))
                .body("break_between_sets.minutes", is(1))
                .body("break_between_sets.seconds", is(0))
                .body("is_finished", is(false))
                .body("sets[0].id", is("00000000-0000-0000-0002-020101010101"))
                .body("sets[0].reps", is(8))
                .body("sets[0].weight", is(40.0f))
                .body("sets[0].is_finished", is(false))
                .body("sets[0].attributes.size()", is(0))
                //
                .body("sets[1].id", is("00000000-0000-0000-0002-020101010102"))
                .body("sets[1].reps", is(8))
                .body("sets[1].weight", is(40.0f))
                .body("sets[1].is_finished", is(false))
                .body("sets[1].attributes.size()", is(0))
                //
                .body("sets[2].id", is("00000000-0000-0000-0002-020101010103"))
                .body("sets[2].reps", is(8))
                .body("sets[2].weight", is(40.0f))
                .body("sets[2].is_finished", is(false))
                .body("sets[2].attributes.size()", is(0))
                //
                .body("history.size()", is(0))
        ;
    }
}
