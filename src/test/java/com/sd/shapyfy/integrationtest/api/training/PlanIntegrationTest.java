package com.sd.shapyfy.integrationtest.api.training;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.PostgresTrainingRepository;
import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType.REST_DAY;
import static com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType.TRAINING_DAY;
import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class PlanIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PostgresTrainingRepository trainingRepository;

    @Test
    @DisplayName("Trainee create plan EP[POST:/v1/trainings]")
    void initializeTraining() {

        String trainingId = as(NEW_USER.getTestUser()).assertRequest($ -> $
                        .body("""
                                    {
                                      "name": "Push Pull",
                                      "day_configurations": [
                                        {
                                          "name": "PUSH",
                                          "type": "TRAINING_DAY",
                                          "selections": [
                                            {
                                              "exercise_id": "00000000-0000-0000-0000-000000000001",
                                              "sets_amount": 3,
                                              "reps_amount": 8,
                                              "weight_amount": 25.5,
                                              "rest_between_sets_second": 120
                                            },
                                            {
                                              "exercise_id": "00000000-0000-0000-0000-000000000003",
                                              "sets_amount": 4,
                                              "reps_amount": 6,
                                              "rest_between_sets_second": 90
                                            },
                                            {
                                              "exercise_id": "00000000-0000-0000-0000-000000000005",
                                              "sets_amount": 3,
                                              "reps_amount": 12,
                                              "weight_amount": 75,
                                              "rest_between_sets_second": 180
                                            }
                                          ]
                                        },
                                        {
                                          "name": "PULL",
                                          "type": "TRAINING_DAY",
                                          "selections": [
                                            {
                                              "exercise_id": "00000000-0000-0000-0000-000000000002",
                                              "sets_amount": 3,
                                              "reps_amount": 8,
                                              "weight_amount": 25.5,
                                              "rest_between_sets_second": 120
                                            }
                                          ]
                                        },
                                        {
                                          "type": "REST_DAY",
                                          "selections": []
                                        }
                                      ]
                                    }
                                """)
                        .post("/v1/plans"))
                //
                .statusCode(200)
                .body("id", notNullValue())
                //
                .body("training_days[0].id", notNullValue())
                .body("training_days[0].name", is("PUSH"))
                .body("training_days[0].type", is("TRAINING_DAY"))
                .body("training_days[0].exercises[0].exercise_name", is("TEST_EXERCISE_1"))
                .body("training_days[0].exercises[0].sets_amount", is(3))
                .body("training_days[0].exercises[0].reps_amount", is(8))
                .body("training_days[0].exercises[0].weight_amount", is(25.5f))
                .body("training_days[0].exercises[0].rest_between_sets_second", is(120))

                .body("training_days[0].exercises[1].exercise_name", is("TEST_EXERCISE_3"))
                .body("training_days[0].exercises[1].sets_amount", is(4))
                .body("training_days[0].exercises[1].reps_amount", is(6))
                .body("training_days[0].exercises[1].weight_amount", nullValue())
                .body("training_days[0].exercises[1].rest_between_sets_second", is(90))

                .body("training_days[0].exercises[2].exercise_name", is("TEST_EXERCISE_5"))
                .body("training_days[0].exercises[2].sets_amount", is(3))
                .body("training_days[0].exercises[2].reps_amount", is(12))
                .body("training_days[0].exercises[2].weight_amount", is(75.0f))
                .body("training_days[0].exercises[2].rest_between_sets_second", is(180))
                //
                .body("training_days[1].id", notNullValue())
                .body("training_days[1].name", is("PULL"))
                .body("training_days[1].type", is("TRAINING_DAY"))
                .body("training_days[1].exercises[0].exercise_name", is("TEST_EXERCISE_2"))
                .body("training_days[1].exercises[0].sets_amount", is(3))
                .body("training_days[1].exercises[0].reps_amount", is(8))
                .body("training_days[1].exercises[0].weight_amount", is(25.5f))
                .body("training_days[1].exercises[0].rest_between_sets_second", is(120))
                //
                .body("training_days[2].id", notNullValue())
                .body("training_days[2].name", nullValue()) //TODO should be named as rest in localized value ;)
                .body("training_days[2].type", is("REST_DAY"))
                .body("training_days[2].exercises", is(emptyIterable()))
                //
                .and().extract().body().path("id");

        assertThat(trainingRepository.findById(UUID.fromString(trainingId)))
                .isPresent()
                .get()
                .satisfies(plan -> {
                    assertThat(plan.getSessions().size()).isEqualTo(1);
                    //
                    assertThat(plan.getSessions().get(0)).satisfies(session -> {
                        assertThat(session.getState()).isEqualTo(SessionState.DRAFT);
                        assertThat(session.getSessionParts().size()).isEqualTo(3);
                        //
                        assertThat(session.getSessionParts()).asList()
                                //
                                .extracting("type")
                                .containsAll(List.of(TRAINING_DAY, TRAINING_DAY, REST_DAY));
                    });
                });

    }

    @Test
    @DisplayName("Trainee activate EP[PATCH:/v1/trainings/{trainingId}/plans]")
    void activateTraining() {
        UUID trainingId = UUID.fromString("00000000-0000-0000-0000-000000000201");
        as(USER_WITH_DRAFT_TRAINING.getTestUser()).assertRequest($ -> $
                        .body("""
                                {
                                  "start_date": "2023-01-01",
                                  "activate_by_session_day_id": "00000000-0000-0000-0000-000002010101"
                                }
                                """)
                        .patch("/v1/plans/{trainingId}/activations", trainingId))
                //
                .statusCode(200);

           assertThat(trainingRepository.findById(trainingId))
                   .isPresent()
                   .get()
                   .satisfies(plan -> {
                       assertThat(plan.getSessions().stream().filter(s -> s.getState() == SessionState.ACTIVE).findFirst())
                               .isPresent()
                               .get()
                               .satisfies(activatedSession -> {
                                   assertThat(activatedSession.getSessionParts()).asList()
                                           //
                                           .extracting("date")
                                           .containsAll(List.of(date(1), date(2), date(3)));
                               });

                       assertThat(plan.getSessions().stream().filter(s -> s.getState() == SessionState.FOLLOW_UP).findFirst())
                               .isPresent()
                               .get()
                               .satisfies(activatedSession -> {
                                   assertThat(activatedSession.getSessionParts()).asList()
                                           //
                                           .extracting("date")
                                           .containsAll(List.of(date(4), date(5), date(6)));
                               });
                   });
    }

    //TODO Move to common test
    LocalDate date(int day) {
        return LocalDate.of(2023, 1, day);
    }
}
