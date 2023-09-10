package com.sd.shapyfy.integrationtest.api.dashboard;

import com.sd.shapyfy.integrationTestTool.AbstractIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.NEW_USER;
import static com.sd.shapyfy.integrationTestTool.spring.security.TestUser.PredefinedUsers.USER_WITH_ACTIVE_TRAINING;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class DashboardIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("User dashboard for new user EP[GET:/v1/dashboard")
    void fetchUserEmptyDashboard() {
        as(NEW_USER.getTestUser()).assertRequest($ -> $
                        .param("from-date", "2023-01-01")
                        .param("to-date", "2023-01-04")
                        .get("/v1/dashboard"))
                //
                .statusCode(200)
                .body("user_meta_data.state", is("NO_TRAINING"))
                .body("day_states[0].id", nullValue())
                .body("day_states[0].training_id", nullValue())
                .body("day_states[0].date", is("2023-01-01"))
                .body("day_states[0].state", is("NO_TRAINING"))
                //
                .body("day_states[1].id", nullValue())
                .body("day_states[1].training_id", nullValue())
                .body("day_states[1].date", is("2023-01-02"))
                .body("day_states[1].state", is("NO_TRAINING"))
                //
                .body("day_states[2].id", nullValue())
                .body("day_states[2].training_id", nullValue())
                .body("day_states[2].date", is("2023-01-03"))
                .body("day_states[2].state", is("NO_TRAINING"))
                //
                .body("day_states[3].id", nullValue())
                .body("day_states[3].training_id", nullValue())
                .body("day_states[3].date", is("2023-01-04"))
                .body("day_states[3].state", is("NO_TRAINING"))
        ;
    }

    @Test
    @DisplayName("User dashboard EP[GET:/v1/dashboard")
    void fetchUserDashboard() {
        as(USER_WITH_ACTIVE_TRAINING.getTestUser()).assertRequest($ -> $
                        .param("from-date", "2023-01-01")
                        .param("to-date", "2023-01-10")
                        .get("/v1/dashboard"))
                //
                .statusCode(200)
                //
                .body("day_states[0].id", is("00000000-0000-0000-0002-000002010101"))
                .body("day_states[0].training_id", is("00000000-0000-0000-0002-000000000201"))
                .body("day_states[0].date", is("2023-01-01"))
                .body("day_states[0].state", is("TRAINING_DAY"))
                //
                .body("day_states[1].id", is("00000000-0000-0000-0002-000002010102"))
                .body("day_states[1].training_id", is("00000000-0000-0000-0002-000000000201"))
                .body("day_states[1].date", is("2023-01-02"))
                .body("day_states[1].state", is("REST_DAY"))
                //
                .body("day_states[2].id", is("00000000-0000-0000-0002-000002010103"))
                .body("day_states[2].training_id", is("00000000-0000-0000-0002-000000000201"))
                .body("day_states[2].date", is("2023-01-03"))
                .body("day_states[2].state", is("TRAINING_DAY"))
                //
                .body("day_states[3].id", is("00000000-0000-0000-0002-000002010104"))
                .body("day_states[3].training_id", is("00000000-0000-0000-0002-000000000201"))
                .body("day_states[3].date", is("2023-01-04"))
                .body("day_states[3].state", is("REST_DAY"))
                //
                .body("day_states[4].id", is("00000000-0000-0000-0002-000002010201"))
                .body("day_states[4].training_id", is("00000000-0000-0000-0002-000000000201"))
                .body("day_states[4].date", is("2023-01-05"))
                .body("day_states[4].state", is("TRAINING_DAY"))
        ;
    }

    @Test
    @DisplayName("Trainee get dashboard for date EP[GET:/v1/dashboard/{date}")
    void getDashboardForSessionPart() {
        as(USER_WITH_ACTIVE_TRAINING.getTestUser()).assertRequest($ -> $
                        .get("/v1/dashboard/2023-01-03"))
                //
                .statusCode(200);
    }
}
