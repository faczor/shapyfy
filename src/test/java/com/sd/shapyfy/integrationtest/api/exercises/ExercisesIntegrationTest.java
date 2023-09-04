package com.sd.shapyfy.integrationtest.api.exercises;

import com.sd.shapyfy.boundary.api.exercises.Creator;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.component.PostgresExerciseRepository;
import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import com.sd.shapyfy.integrationTestTool.spring.security.TestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.NEW_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class ExercisesIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    PostgresExerciseRepository exerciseRepository;

    @Test
    @DisplayName("Search all available exercises EP[GET:/v1/exercises]")
    void fetchAvailableExercises() {
        as(NEW_USER.getTestUser()).assertRequest($ -> $
                        .get("/v1/exercises"))
                //
                .statusCode(200)
                .body("items.size()", not(0));
    }

    @Test
    @DisplayName("Create exercise EP[POST:/v1/exercises]")
    void createExercise() {
        String createdExerciseId = as(NEW_USER.getTestUser()).assertRequest($ -> $
                        .body("""
                                {
                                  "name": "Barbel curl"
                                }
                                """)
                        .post("/v1/exercises"))
                //
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", is("Barbel curl"))
                //
                .and().extract().body().path("id");

        assertThat(exerciseRepository.findById(UUID.fromString(createdExerciseId)))
                .isPresent()
                .get()
                .satisfies(exercise -> {
                    assertThat(exercise.getCreator()).isEqualTo(Creator.COMMUNITY);
                    assertThat(exercise.getName()).isEqualTo("Barbel curl");
                });
    }
}
