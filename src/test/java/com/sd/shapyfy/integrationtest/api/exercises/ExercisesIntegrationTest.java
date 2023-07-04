package com.sd.shapyfy.integrationtest.api.exercises;

import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.DEFAULT;
import static org.hamcrest.Matchers.is;

public class ExercisesIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Search all available exercises EP[PATCH:/v1/exercises")
    void fetchAvailableExercises() {
        as(DEFAULT.getTestUser()).assertRequest($ -> $
                        .get("/v1/exercises"))
                //
                .statusCode(200)
                .body("items.size()", is(36));
    }
}
