package com.sd.shapyfy.integrationtest.api.training;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.PostgresTrainingRepository;
import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Disabled;
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

class PlanIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PostgresTrainingRepository trainingRepository;

    @Test
    @Disabled
    @DisplayName("Trainee fetch all plans EP[GET:/v1/plans")
    void getAllPlans() {
        as(USER_WITH_ACTIVE_TRAINING.getTestUser()).assertRequest($ -> $
                        .get("/v1/plans"))
                //
                .statusCode(200)
                .body("plans[0].id", is("00000000-0000-0000-0000-000000000301"))
                .body("plans[0].state", is("NOT_STARTED"))
                .body("plans[0].training_days[0].id", is("00000000-0000-0000-0000-000003010101"))
                .body("plans[0].training_days[0].name", is("PULL"))
                .body("plans[0].training_days[0].type", is("TRAINING_DAY"))
                .body("plans[0].training_days[0].exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
                .body("plans[0].training_days[0].exercises[0].exercise_name", is("TEST_EXERCISE_1"))
                .body("plans[0].training_days[0].exercises[0].sets_amount", is(3))
                .body("plans[0].training_days[0].exercises[0].reps_amount", is(8))
                .body("plans[0].training_days[0].exercises[0].weight_amount", is(25.5f))
                .body("plans[0].training_days[0].exercises[0].rest_between_sets_second", is(120))
                //
                .body("plans[0].training_days[0].exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000003"))
                .body("plans[0].training_days[0].exercises[1].exercise_name", is("TEST_EXERCISE_3"))
                .body("plans[0].training_days[0].exercises[1].sets_amount", is(4))
                .body("plans[0].training_days[0].exercises[1].reps_amount", is(6))
                .body("plans[0].training_days[0].exercises[1].weight_amount", is(80.0f))
                .body("plans[0].training_days[0].exercises[1].rest_between_sets_second", is(60))
                //
                .body("plans[0].training_days[0].exercises[2].exercise_id", is("00000000-0000-0000-0000-000000000005"))
                .body("plans[0].training_days[0].exercises[2].exercise_name", is("TEST_EXERCISE_5"))
                .body("plans[0].training_days[0].exercises[2].sets_amount", is(3))
                .body("plans[0].training_days[0].exercises[2].reps_amount", is(8))
                .body("plans[0].training_days[0].exercises[2].weight_amount", nullValue())
                .body("plans[0].training_days[0].exercises[2].rest_between_sets_second", is(150))
                //
                //
                .body("plans[0].training_days[1].id", is("00000000-0000-0000-0000-000003010102"))
                .body("plans[0].training_days[1].name", is("PUSH"))
                .body("plans[0].training_days[1].type", is("TRAINING_DAY"))
                .body("plans[0].training_days[1].exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
                .body("plans[0].training_days[1].exercises[0].exercise_name", is("TEST_EXERCISE_1"))
                .body("plans[0].training_days[1].exercises[0].sets_amount", is(3))
                .body("plans[0].training_days[1].exercises[0].reps_amount", is(12))
                .body("plans[0].training_days[1].exercises[0].weight_amount", is(10.0f))
                .body("plans[0].training_days[1].exercises[0].rest_between_sets_second", is(120))
                //
                .body("plans[0].training_days[1].exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000002"))
                .body("plans[0].training_days[1].exercises[1].exercise_name", is("TEST_EXERCISE_2"))
                .body("plans[0].training_days[1].exercises[1].sets_amount", is(3))
                .body("plans[0].training_days[1].exercises[1].reps_amount", is(10))
                .body("plans[0].training_days[1].exercises[1].weight_amount", is(30.0f))
                .body("plans[0].training_days[1].exercises[1].rest_between_sets_second", is(60))
                //
                .body("plans[0].training_days[1].exercises[2].exercise_id", is("00000000-0000-0000-0000-000000000003"))
                .body("plans[0].training_days[1].exercises[2].exercise_name", is("TEST_EXERCISE_3"))
                .body("plans[0].training_days[1].exercises[2].sets_amount", is(1))
                .body("plans[0].training_days[1].exercises[2].reps_amount", is(8))
                .body("plans[0].training_days[1].exercises[2].weight_amount", nullValue())
                .body("plans[0].training_days[1].exercises[2].rest_between_sets_second", is(150))
                //
                //
                .body("plans[0].training_days[2].id", is("00000000-0000-0000-0000-000003010103"))
                .body("plans[0].training_days[2].name", is("REST"))
                .body("plans[0].training_days[2].type", is("REST_DAY"))
        ;
    }

    @Test
    @DisplayName("Trainee fetch plan by id EP[GET:/v1/plans/{id}")
    void getPlanById() {
        as(USER_WITH_DRAFT_TRAINING.getTestUser()).assertRequest($ -> $
                        .get("/v1/plans/00000000-0000-0000-0001-000000000101"))
                //
                .statusCode(200)
                .body("id", is("00000000-0000-0000-0001-000000000101"))
                .body("name", is("FBW"))
                .body("state", is("NOT_STARTED"))
                .body("configuration.id", is("00000000-0000-0000-0000-000000000001"))
                .body("configuration.training_days[0].id", is("00000000-0000-0000-0000-000000000101"))
                .body("configuration.training_days[0].name", is("FBW 1"))
                .body("configuration.training_days[0].type", is("TRAINING_DAY"))
                .body("configuration.training_days[0].exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
                .body("configuration.training_days[0].exercises[0].exercise_name", is("TEST_EXERCISE_1"))
                .body("configuration.training_days[0].exercises[0].sets_amount", is(3))
                .body("configuration.training_days[0].exercises[0].reps_amount", is(8))
                .body("configuration.training_days[0].exercises[0].weight_amount", is(40.0f))
                .body("configuration.training_days[0].exercises[0].rest_between_sets.minutes", is(1))
                .body("configuration.training_days[0].exercises[0].rest_between_sets.seconds", is(0))
                //
                .body("configuration.training_days[0].exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000002"))
                .body("configuration.training_days[0].exercises[1].exercise_name", is("TEST_EXERCISE_2"))
                .body("configuration.training_days[0].exercises[1].sets_amount", is(4))
                .body("configuration.training_days[0].exercises[1].reps_amount", is(6))
                .body("configuration.training_days[0].exercises[1].weight_amount", is(60.0f))
                .body("configuration.training_days[0].exercises[1].rest_between_sets.minutes", is(1))
                .body("configuration.training_days[0].exercises[1].rest_between_sets.seconds", is(30))
                //
                .body("configuration.training_days[0].exercises[2].exercise_id", is("00000000-0000-0000-0000-000000000003"))
                .body("configuration.training_days[0].exercises[2].exercise_name", is("TEST_EXERCISE_3"))
                .body("configuration.training_days[0].exercises[2].sets_amount", is(4))
                .body("configuration.training_days[0].exercises[2].reps_amount", is(6))
                .body("configuration.training_days[0].exercises[2].weight_amount", is(110.5f))
                .body("configuration.training_days[0].exercises[2].rest_between_sets.minutes", is(2))
                .body("configuration.training_days[0].exercises[2].rest_between_sets.seconds", is(0))
                //
                .body("configuration.training_days[1].id", is("00000000-0000-0000-0000-000000000102"))
                .body("configuration.training_days[1].name", is("ODPOCZYNEK"))
                .body("configuration.training_days[1].type", is("REST_DAY"))
                .body("configuration.training_days[1].exercises.size()", is(0))
                //
                .body("configuration.training_days[2].id", is("00000000-0000-0000-0000-000000000103"))
                .body("configuration.training_days[2].name", is("FBW 2"))
                .body("configuration.training_days[2].type", is("TRAINING_DAY"))
                .body("configuration.training_days[2].exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000004"))
                .body("configuration.training_days[2].exercises[0].exercise_name", is("TEST_EXERCISE_4"))
                .body("configuration.training_days[2].exercises[0].sets_amount", is(2))
                .body("configuration.training_days[2].exercises[0].reps_amount", is(5))
                .body("configuration.training_days[2].exercises[0].weight_amount", is(10.5f))
                .body("configuration.training_days[2].exercises[0].rest_between_sets.minutes", is(0))
                .body("configuration.training_days[2].exercises[0].rest_between_sets.seconds", is(30))
                //
                .body("configuration.training_days[2].exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000005"))
                .body("configuration.training_days[2].exercises[1].exercise_name", is("TEST_EXERCISE_5"))
                .body("configuration.training_days[2].exercises[1].sets_amount", is(3))
                .body("configuration.training_days[2].exercises[1].reps_amount", is(8))
                .body("configuration.training_days[2].exercises[1].weight_amount", is(50.0f))
                .body("configuration.training_days[2].exercises[1].rest_between_sets.minutes", is(0))
                .body("configuration.training_days[2].exercises[1].rest_between_sets.seconds", is(50))
                //
                .body("configuration.training_days[3].id", is("00000000-0000-0000-0000-000000000104"))
                .body("configuration.training_days[3].name", is("ODPOCZYNEK"))
                .body("configuration.training_days[3].type", is("REST_DAY"))
                .body("configuration.training_days[3].exercises.size()", is(0))
                //
                .body("configuration.training_days[4].id", is("00000000-0000-0000-0000-000000000105"))
                .body("configuration.training_days[4].name", is("ODPOCZYNEK"))
                .body("configuration.training_days[4].type", is("REST_DAY"))
                .body("configuration.training_days[4].exercises.size()", is(0))
        ;

    }

    @Test
    @DisplayName("Trainee create plan EP[POST:/v1/plans]")
    void initializeTraining() {

        String trainingId = as(NEW_USER.getTestUser()).assertRequest($ -> $
                        .body("""
                                    {
                                      "name": "Push Pull",
                                      "exercise_attributes": ["EXERCISE_ATTRIBUTE_1", "EXERCISE_ATTRIBUTE_2"],
                                      "set_attributes": ["SET_ATTRIBUTE_1", "SET_ATTRIBUTE_2"],
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
                                              "rest_between_sets": {
                                                "minutes": 1,
                                                "seconds": 0
                                              }
                                            },
                                            {
                                              "exercise_id": "00000000-0000-0000-0000-000000000003",
                                              "sets_amount": 4,
                                              "reps_amount": 6,
                                              "rest_between_sets": {
                                                "minutes": 1,
                                                "seconds": 30
                                              }
                                            },
                                            {
                                              "exercise_id": "00000000-0000-0000-0000-000000000005",
                                              "sets_amount": 3,
                                              "reps_amount": 12,
                                              "weight_amount": 75,
                                              "rest_between_sets": {
                                                "minutes": 2,
                                                "seconds": 15
                                              }
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
                                              "rest_between_sets": {
                                                "minutes": 3,
                                                "seconds": 0
                                              }
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
                .body("name", is("Push Pull"))
                .body("state", is("NOT_STARTED"))
                .body("configuration.id", notNullValue())
                .body("configuration.training_days[0].id", notNullValue())
                .body("configuration.training_days[0].name", is("PUSH"))
                .body("configuration.training_days[0].type", is("TRAINING_DAY"))
                .body("configuration.training_days[0].exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
                .body("configuration.training_days[0].exercises[0].exercise_name", is("TEST_EXERCISE_1"))
                .body("configuration.training_days[0].exercises[0].sets_amount", is(3))
                .body("configuration.training_days[0].exercises[0].reps_amount", is(8))
                .body("configuration.training_days[0].exercises[0].weight_amount", is(25.5f))
                .body("configuration.training_days[0].exercises[0].rest_between_sets.minutes", is(1))
                .body("configuration.training_days[0].exercises[0].rest_between_sets.seconds", is(0))
                //
                .body("configuration.training_days[0].exercises[1].exercise_id", is("00000000-0000-0000-0000-000000000003"))
                .body("configuration.training_days[0].exercises[1].exercise_name", is("TEST_EXERCISE_3"))
                .body("configuration.training_days[0].exercises[1].sets_amount", is(4))
                .body("configuration.training_days[0].exercises[1].reps_amount", is(6))
                .body("configuration.training_days[0].exercises[1].weight_amount", is(nullValue()))
                .body("configuration.training_days[0].exercises[1].rest_between_sets.minutes", is(1))
                .body("configuration.training_days[0].exercises[1].rest_between_sets.seconds", is(30))
                //
                .body("configuration.training_days[0].exercises[2].exercise_id", is("00000000-0000-0000-0000-000000000005"))
                .body("configuration.training_days[0].exercises[2].exercise_name", is("TEST_EXERCISE_5"))
                .body("configuration.training_days[0].exercises[2].sets_amount", is(3))
                .body("configuration.training_days[0].exercises[2].reps_amount", is(12))
                .body("configuration.training_days[0].exercises[2].weight_amount", is(75.0f))
                .body("configuration.training_days[0].exercises[2].rest_between_sets.minutes", is(2))
                .body("configuration.training_days[0].exercises[2].rest_between_sets.seconds", is(15))
                //
                //
                .body("configuration.training_days[1].id", notNullValue())
                .body("configuration.training_days[1].name", is("PULL"))
                .body("configuration.training_days[1].type", is("TRAINING_DAY"))
                .body("configuration.training_days[1].exercises[0].exercise_id", is("00000000-0000-0000-0000-000000000002"))
                .body("configuration.training_days[1].exercises[0].exercise_name", is("TEST_EXERCISE_2"))
                .body("configuration.training_days[1].exercises[0].sets_amount", is(3))
                .body("configuration.training_days[1].exercises[0].reps_amount", is(8))
                .body("configuration.training_days[1].exercises[0].weight_amount", is(25.5f))
                .body("configuration.training_days[1].exercises[0].rest_between_sets.minutes", is(3))
                .body("configuration.training_days[1].exercises[0].rest_between_sets.seconds", is(0))
                //
                //
                .body("configuration.training_days[2].id", notNullValue())
                .body("configuration.training_days[2].name", is(nullValue()))
                .body("configuration.training_days[2].type", is("REST_DAY"))
                .body("configuration.training_days[2].exercises.size()", is(0))
                .and().extract().body().path("id");

        //TODO assert configuration state
    }

    @Test
    @DisplayName("Trainee activate EP[PATCH:/v1/plans/{planId}/activations]")
    void activateTraining() {
        UUID trainingId = UUID.fromString("00000000-0000-0000-0001-000000000101");
        as(USER_WITH_DRAFT_TRAINING.getTestUser()).assertRequest($ -> $
                        .body("""
                                {
                                  "start_date": "2023-01-01",
                                  "activate_by_session_day_id": "00000000-0000-0000-0000-000000000101"
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
                                        .extracting("name", "date")
                                        .containsAll(List.of(
                                                Tuple.tuple("FBW 1", date(1)),
                                                Tuple.tuple("ODPOCZYNEK", date(2)),
                                                Tuple.tuple("FBW 2", date(3)),
                                                Tuple.tuple("ODPOCZYNEK", date(4)),
                                                Tuple.tuple("ODPOCZYNEK", date(5))));
                            });

                    assertThat(plan.getSessions().stream().filter(s -> s.getState() == SessionState.FOLLOW_UP).findFirst())
                            .isPresent()
                            .get()
                            .satisfies(followUpSession -> {
                                assertThat(followUpSession.getSessionParts()).asList()
                                        //
                                        .extracting("name", "date")
                                        .containsAll(List.of(
                                                Tuple.tuple("FBW 1", date(6)),
                                                Tuple.tuple("ODPOCZYNEK", date(7)),
                                                Tuple.tuple("FBW 2", date(8)),
                                                Tuple.tuple("ODPOCZYNEK", date(9)),
                                                Tuple.tuple("ODPOCZYNEK", date(10))));
                            });
                });
    }

    //TODO Move to common test
    LocalDate date(int day) {
        return LocalDate.of(2023, 1, day);
    }
}
