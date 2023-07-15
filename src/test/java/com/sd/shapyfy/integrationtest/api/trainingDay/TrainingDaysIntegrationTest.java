package com.sd.shapyfy.integrationtest.api.trainingDay;

import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.PostgresTrainingDayRepository;
import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.DEFAULT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class TrainingDaysIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PostgresTrainingDayRepository trainingDayRepository;

    @Test
    @DisplayName("Trainee select exercises EP[PATCH:/v1/training_days/{training_day_id}")
    void selectExercisesToTraining() {
        UUID trainingDayId = UUID.fromString("00000000-0000-0000-0000-000000000101");

        as(DEFAULT.getTestUser()).assertRequest($ -> $
                        .body("""
                                {
                                  "exercises": [
                                    {
                                      "exercise_id": "00000000-0000-0000-0000-000000000001",
                                      "sets_amount": 3,
                                      "reps_amount": 8,
                                      "weight_amount": 25.5
                                    },
                                    {
                                      "exercise_id": "00000000-0000-0000-0000-000000000003",
                                      "sets_amount": 4,
                                      "reps_amount": 6
                                    },
                                    {
                                      "exercise_id": "00000000-0000-0000-0000-000000000005",
                                      "sets_amount": 3,
                                      "reps_amount": 12,
                                      "weight_amount": 75
                                    }
                                  ]
                                }
                                """)
                        .patch("/v1/training_days/{training_day_id}", trainingDayId))
                //
                .statusCode(200)
                //
                .body("items[0].exercise.id", is("00000000-0000-0000-0000-000000000001"))
                .body("items[0].exercise.name", is("TEST_EXERCISE_1"))
                .body("items[0].exercise_training_attributes.sets_amount", is(3))
                .body("items[0].exercise_training_attributes.reps_amount", is(8))
                .body("items[0].exercise_training_attributes.weight_amount", is(25.5f))
                //
                .body("items[1].exercise.id", is("00000000-0000-0000-0000-000000000003"))
                .body("items[1].exercise.name", is("TEST_EXERCISE_3"))
                .body("items[1].exercise_training_attributes.sets_amount", is(4))
                .body("items[1].exercise_training_attributes.reps_amount", is(6))
                .body("items[1].exercise_training_attributes.weight_amount", nullValue())
                //
                .body("items[2].exercise.id", is("00000000-0000-0000-0000-000000000005"))
                .body("items[2].exercise.name", is("TEST_EXERCISE_5"))
                .body("items[2].exercise_training_attributes.sets_amount", is(3))
                .body("items[2].exercise_training_attributes.reps_amount", is(12))
                .body("items[2].exercise_training_attributes.weight_amount", is(75.0f))
        ;

        assertThat(trainingDayRepository.findById(trainingDayId))
                .isPresent()
                .get()
                .satisfies(trainingDay -> {
                    assertThat(trainingDay.getSessions().get(0).getSessionExercises().size()).isEqualTo(3);
                    //
                    assertThat(trainingDay.getSessions().get(0).getSessionExercises()).asList()
                            //
                            .extracting("setsAmount", "repsAmount", "weightAmount")
                            .containsAll(List.of(
                                    tuple(3, 8, 25.5),
                                    tuple(4, 6, null),
                                    tuple(3, 12, 75.0)
                            ));
                });
    }
}
