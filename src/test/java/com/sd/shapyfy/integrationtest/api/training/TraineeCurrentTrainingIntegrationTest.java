package com.sd.shapyfy.integrationtest.api.training;

import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.NEW_USER;
import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.USER_WITH_ACTIVE_TRAINING;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

public class TraineeCurrentTrainingIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Trainnee display current training EP[POST:/v1/current-trainings]")
    void fetchCurrentTraining() {
        as(USER_WITH_ACTIVE_TRAINING.getTestUser()).assertRequest($ -> $.get("/v1/current-trainings"))
                //
                .statusCode(200)
                //
                .body("training_id", is("00000000-0000-0000-0000-000000000200"))
                .body("name", is("PUSH A,PULL A,LEGS A,null,PUSH B,PULL B,LEGS B,null"))
                //
                .body("training_days[0].training_day_id", is("00000000-0000-0000-0000-000000000201"))
                .body("training_days[0].name", is("PUSH A"))
                .body("training_days[0].day_type", is("TRAINING"))
                .body("training_days[0].day_of_week", is("MONDAY"))
                //
                .body("training_days[0].training_exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
                .body("training_days[0].training_exercises[0].name", is("TEST_EXERCISE_1"))
                .body("training_days[0].training_exercises[0].sets", is(3))
                .body("training_days[0].training_exercises[0].reps", is(8))
                .body("training_days[0].training_exercises[0].weight", is(25.5f))
                //
                .body("training_days[0].training_exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000003"))
                .body("training_days[0].training_exercises[1].name", is("TEST_EXERCISE_3"))
                .body("training_days[0].training_exercises[1].sets", is(4))
                .body("training_days[0].training_exercises[1].reps", is(6))
                .body("training_days[0].training_exercises[1].weight", is(80.0f))
                //
                .body("training_days[0].training_exercises[2].exercise_id", is("00000000-0000-0000-0000-000000000005"))
                .body("training_days[0].training_exercises[2].name", is("TEST_EXERCISE_5"))
                .body("training_days[0].training_exercises[2].sets", is(3))
                .body("training_days[0].training_exercises[2].reps", is(8))
                .body("training_days[0].training_exercises[2].weight", nullValue())
                //
                //
                .body("training_days[1].training_day_id", is("00000000-0000-0000-0000-000000000202"))
                .body("training_days[1].name", is("PULL A"))
                .body("training_days[1].day_type", is("TRAINING"))
                .body("training_days[1].day_of_week", is("TUESDAY"))
                //
                .body("training_days[1].training_exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
                .body("training_days[1].training_exercises[0].name", is("TEST_EXERCISE_1"))
                .body("training_days[1].training_exercises[0].sets", is(3))
                .body("training_days[1].training_exercises[0].reps", is(12))
                .body("training_days[1].training_exercises[0].weight", is(10.0f))
                //
                .body("training_days[1].training_exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000002"))
                .body("training_days[1].training_exercises[1].name", is("TEST_EXERCISE_2"))
                .body("training_days[1].training_exercises[1].sets", is(3))
                .body("training_days[1].training_exercises[1].reps", is(10))
                .body("training_days[1].training_exercises[1].weight", is(30.0f))
                //
                .body("training_days[1].training_exercises[2].exercise_id", is("00000000-0000-0000-0000-000000000003"))
                .body("training_days[1].training_exercises[2].name", is("TEST_EXERCISE_3"))
                .body("training_days[1].training_exercises[2].sets", is(1))
                .body("training_days[1].training_exercises[2].reps", is(8))
                .body("training_days[1].training_exercises[2].weight", nullValue())
                //
                //
                .body("training_days[2].training_day_id", is("00000000-0000-0000-0000-000000000203"))
                .body("training_days[2].name", is("LEGS A"))
                .body("training_days[2].day_type", is("TRAINING"))
                .body("training_days[2].day_of_week", is("WEDNESDAY"))
                //
                .body("training_days[2].training_exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000004"))
                .body("training_days[2].training_exercises[0].name", is("TEST_EXERCISE_4"))
                .body("training_days[2].training_exercises[0].sets", is(3))
                .body("training_days[2].training_exercises[0].reps", is(8))
                .body("training_days[2].training_exercises[0].weight", is(25.5f))
                //
                //
                //
                .body("training_days[3].training_day_id", is("00000000-0000-0000-0000-000000000204"))
                .body("training_days[3].name", is("REST_DAY"))
                .body("training_days[3].day_type", is("OFF"))
                .body("training_days[3].day_of_week", is("THURSDAY"))
                .body("training_days[3].training_exercises", is(emptyIterable()))
        ;
    }

    @Test
    @DisplayName("Trainnee display current training EP[POST:/v1/current-trainings]")
    void currentTrainingForNewUser() {
        as(NEW_USER.getTestUser()).assertRequest($ -> $.get("/v1/current-trainings"))
                //
                .statusCode(400)
                .body(is("Current training not found for UserId::00000000000000000"));

    }
}
