package com.sd.shapyfy.integrationtest.api.dashboard;

import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.NEW_USER;
import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.USER_WITH_ACTIVE_TRAINING;
import static org.hamcrest.Matchers.is;

public class DashboardIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("User dashboard for new user EP[GET:/v1/dashboard")
    void fetchUserEmptyDashboard() {
        as(NEW_USER.getTestUser()).assertRequest($ -> $
                        .param("from-date", "2023-01-01")
                        .param("to-date", "2023-01-04")
                        .get("/v1/dashboard"))
                //
                .statusCode(200);
    }

    @Test
    @DisplayName("User dashboard EP[GET:/v1/dashboard")
    void fetchUserDashboard() {
        as(USER_WITH_ACTIVE_TRAINING.getTestUser()).assertRequest($ -> $
                        .param("from-date", "2023-01-01")
                        .param("to-date", "2023-01-12")
                        .get("/v1/dashboard"))
                //
                .statusCode(200)
                //
                .body("day_states[0].date", is("2023-01-01"))
                .body("day_states[0].state", is("TRAINING_DAY"))
                //
                .body("day_states[1].date", is("2023-01-02"))
                .body("day_states[1].state", is("TRAINING_DAY"))
                //
                .body("day_states[2].date", is("2023-01-03"))
                .body("day_states[2].state", is("TRAINING_DAY"))
                //
                .body("day_states[3].date", is("2023-01-04"))
                .body("day_states[3].state", is("NO_TRAINING")) //TODO change implementation
                //
                .body("day_states[4].date", is("2023-01-05"))
                .body("day_states[4].state", is("TRAINING_DAY"))
                //
                .body("day_states[5].date", is("2023-01-06"))
                .body("day_states[5].state", is("TRAINING_DAY"))
                //
                .body("day_states[6].date", is("2023-01-07"))
                .body("day_states[6].state", is("TRAINING_DAY"))
                //
                .body("day_states[7].date", is("2023-01-08"))
                .body("day_states[7].state", is("REST_DAY"))
                //
                .body("day_states[8].date", is("2023-01-09"))
                .body("day_states[8].state", is("TRAINING_DAY"))
                //
                .body("day_states[9].date", is("2023-01-10"))
                .body("day_states[9].state", is("TRAINING_DAY"))
                //
                .body("day_states[10].date", is("2023-01-11"))
                .body("day_states[10].state", is("TRAINING_DAY"))
                //
                .body("day_states[11].date", is("2023-01-12"))
                .body("day_states[11].state", is("REST_DAY"))

        ;
    }
}
