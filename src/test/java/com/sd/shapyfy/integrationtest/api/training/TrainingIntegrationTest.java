package com.sd.shapyfy.integrationtest.api.training;

import com.sd.shapyfy.infrastructure.postgres.trainings.PostgresTrainingRepository;
import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.DEFAULT;
import static java.time.DayOfWeek.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.hamcrest.Matchers.*;

public class TrainingIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PostgresTrainingRepository trainingRepository;

    @Test
    @DisplayName("Trainee create training EP[POST:/v1/trainings]")
    void initializeTraining() {

        String trainingId = as(DEFAULT.getTestUser()).assertRequest($ -> $
                        .body("""
                                {
                                  "name": null,
                                  "day_configurations": [
                                    {
                                      "name": "PUSH A",
                                      "type": "TRAINING",
                                      "day_of_week": "MONDAY"
                                    },
                                    {
                                      "name": "PULL A",
                                      "type": "TRAINING",
                                      "day_of_week": "TUESDAY"
                                    },
                                    {
                                      "name": "LEGS A",
                                      "type": "TRAINING",
                                      "day_of_week": "WEDNESDAY"
                                    },
                                    {
                                      "type": "OFF",
                                      "day_of_week": "THURSDAY"
                                    }
                                  ]
                                }
                                """)
                        .post("/v1/trainings"))
                //
                .statusCode(200)
                .body("id", notNullValue())
                //
                .body("training_days[0].id", notNullValue())
                .body("training_days[0].name", is("PUSH A"))
                .body("training_days[0].day_of_week", is("MONDAY"))
                .body("training_days[0].type", is("TRAINING"))
                .body("training_days[0].exercises", is(emptyIterable()))
                //
                .body("training_days[1].id", notNullValue())
                .body("training_days[1].name", is("PULL A"))
                .body("training_days[1].day_of_week", is("TUESDAY"))
                .body("training_days[1].type", is("TRAINING"))
                .body("training_days[1].exercises", is(emptyIterable()))
                //
                .body("training_days[2].id", notNullValue())
                .body("training_days[2].name", is("LEGS A"))
                .body("training_days[2].day_of_week", is("WEDNESDAY"))
                .body("training_days[2].type", is("TRAINING"))
                .body("training_days[2].exercises", is(emptyIterable()))
                //
                .body("training_days[3].id", notNullValue())
                .body("training_days[3].name", is("REST_DAY"))
                .body("training_days[3].day_of_week", is("THURSDAY"))
                .body("training_days[3].type", is("OFF"))
                .body("training_days[3].exercises", is(emptyIterable()))
                //
                .and().extract().body().path("id");

        assertThat(trainingRepository.findById(UUID.fromString(trainingId)))
                .isPresent()
                .get()
                .satisfies(training -> {
                    assertThat(training.getDays().size()).isEqualTo(4);
                    assertThat(training.getDays()).asList()
                            //
                            .extracting("name", "day", "order", "isOff")
                            .containsAll(List.of(
                                    tuple("PUSH A", MONDAY, 0, false),
                                    tuple("PULL A", TUESDAY, 1, false),
                                    tuple("LEGS A", WEDNESDAY, 2, false),
                                    tuple("REST_DAY", THURSDAY, 3, true)
                            ));
                });

    }
}
