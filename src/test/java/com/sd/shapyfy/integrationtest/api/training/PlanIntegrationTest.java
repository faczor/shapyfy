package com.sd.shapyfy.integrationtest.api.training;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.component.PostgresTrainingDayRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.PostgresTrainingRepository;
import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.hamcrest.Matchers.*;

public class PlanIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PostgresTrainingRepository trainingRepository;


    @Autowired
    PostgresTrainingDayRepository trainingDayRepository;

    @Test
    @DisplayName("Trainee create plan EP[POST:/v1/trainings]")
    void initializeTraining() {

        String trainingId = as(NEW_USER.getTestUser()).assertRequest($ -> $
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
                                      "type": "REST",
                                      "day_of_week": "THURSDAY"
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
                .body("training_days[0].name", is("PUSH A"))
                .body("training_days[0].type", is("TRAINING"))
                .body("training_days[0].exercises", is(emptyIterable()))
                //
                .body("training_days[1].id", notNullValue())
                .body("training_days[1].name", is("PULL A"))
                .body("training_days[1].type", is("TRAINING"))
                .body("training_days[1].exercises", is(emptyIterable()))
                //
                .body("training_days[2].id", notNullValue())
                .body("training_days[2].name", is("LEGS A"))
                .body("training_days[2].type", is("TRAINING"))
                .body("training_days[2].exercises", is(emptyIterable()))
                //
                .body("training_days[3].id", notNullValue())
                .body("training_days[3].name", is("REST_DAY"))
                .body("training_days[3].type", is("REST"))
                .body("training_days[3].exercises", is(emptyIterable()))
                //
                .and().extract().body().path("id");

        assertThat(trainingRepository.findById(UUID.fromString(trainingId)))
                .isPresent()
                .get()
                .satisfies(plan -> {
                    assertThat(plan.getDays().size()).isEqualTo(4);
                    assertThat(plan.getDays()).asList()
                            //
                            .extracting("name", "isOff")
                            .containsAll(List.of(
                                    tuple("PUSH A", false),
                                    tuple("PULL A", false),
                                    tuple("LEGS A", false),
                                    tuple("REST_DAY", true)
                            ));
                });

    }

    @Test
    @DisplayName("Trainee activate EP[PATCH:/v1/trainings/{trainingId}/plans]")
    void activateTraining() {
        UUID trainingId = UUID.fromString("00000000-0000-0000-0000-000000000200");
        as(USER_WITH_DRAFT_TRAINING.getTestUser()).assertRequest($ -> $
                        .body("""
                                {
                                  "start_date": "2023-01-01",
                                  "initialize_training_with_day_id": "00000000-0000-0000-0000-000000000201"
                                }
                                """)
                        .patch("/v1/plans/{trainingId}/activations", trainingId))
                //
                .statusCode(200);

        assertThat(trainingRepository.findById(trainingId))
                .isPresent()
                .get()
                .satisfies(plan -> {
                    List<SessionEntity> activatedSessions = plan.getDays().stream().map(TrainingDayEntity::getSessions)
                            .flatMap(Collection::stream)
                            .filter(s -> s.getState() == SessionState.ACTIVE)
                            .toList();

                    assertThat(activatedSessions).asList()
                            //
                            .extracting("date")
                            .containsAll(List.of(
                                    date(1, 1),
                                    date(2, 1),
                                    date(3, 1)
                            ));

                    List<SessionEntity> followUpSessions = plan.getDays().stream().map(TrainingDayEntity::getSessions)
                            .flatMap(Collection::stream)
                            .filter(s -> s.getState() == SessionState.FOLLOW_UP)
                            .toList();
                    assertThat(followUpSessions).asList()
                            //
                            .extracting("date")
                            .containsAll(List.of(
                                    date(5, 1),
                                    date(6, 1),
                                    date(7, 1)
                            ));
                });
    }

    @Test
    @DisplayName("Trainee select exercises EP[PATCH:/v1/training_days/{plan_id}/configuration_days/{configuration_id}/selections")
    void selectExercisesToTraining() {
        UUID trainingDayId = UUID.fromString("00000000-0000-0000-0000-000000000101");

        as(USER_WITH_INITIALIZED_EMPTY_TRAINING.getTestUser()).assertRequest($ -> $
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
                        .patch("/v1/plans/{plan_id}/configuration_days/{configuration_id}/selections", "00000000-0000-0000-0000-000000000100", trainingDayId))
                //
                .statusCode(200)
                //
                .body("id", is("00000000-0000-0000-0000-000000000101"))
                .body("name", is("PUSH A"))
                //
                .body("trainingDayExercises[0].exercise_id", is("00000000-0000-0000-0000-000000000001"))
                .body("trainingDayExercises[0].exercise_name", is("TEST_EXERCISE_1"))
                .body("trainingDayExercises[0].sets_amount", is(3))
                .body("trainingDayExercises[0].reps_amount", is(8))
                .body("trainingDayExercises[0].weight_amount", is(25.5f))
                //
                .body("trainingDayExercises[1].exercise_id", is("00000000-0000-0000-0000-000000000003"))
                .body("trainingDayExercises[1].exercise_name", is("TEST_EXERCISE_3"))
                .body("trainingDayExercises[1].sets_amount", is(4))
                .body("trainingDayExercises[1].reps_amount", is(6))
                .body("trainingDayExercises[1].weight_amount", nullValue())
                //
                .body("trainingDayExercises[2].exercise_id", is("00000000-0000-0000-0000-000000000005"))
                .body("trainingDayExercises[2].exercise_name", is("TEST_EXERCISE_5"))
                .body("trainingDayExercises[2].sets_amount", is(3))
                .body("trainingDayExercises[2].reps_amount", is(12))
                .body("trainingDayExercises[2].weight_amount", is(75.0f))
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

    LocalDate date(int day, int month) {
        return LocalDate.of(2023, month, day);
    }
}
