package com.sd.shapyfy.integrationtest.api.training;

import com.sd.shapyfy.domain.session.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.PostgresTrainingRepository;
import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.NEW_USER;
import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.USER_WITH_DRAFT_TRAINING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.hamcrest.Matchers.*;

public class PlanIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PostgresTrainingRepository trainingRepository;

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
    @DisplayName("Trainee activate EP[PUT:/v1/trainings/{trainingId}/plans]")
    void activateTraining() {
        UUID trainingId = UUID.fromString("00000000-0000-0000-0000-000000000200");
        as(USER_WITH_DRAFT_TRAINING.getTestUser()).assertRequest($ -> $
                        .body("""
                                {
                                  "start_date": "2023-01-01",
                                  "initialize_training_with_day_id": "00000000-0000-0000-0000-000000000201"
                                }
                                """)
                        .put("/v1/plans/{trainingId}/activations", trainingId))
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

    LocalDate date(int day, int month) {
        return LocalDate.of(2023, month, day);
    }
}
