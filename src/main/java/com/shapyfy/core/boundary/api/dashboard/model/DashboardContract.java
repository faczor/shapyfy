package com.shapyfy.core.boundary.api.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shapyfy.core.util.DateRange;

public record DashboardContract(
        @JsonProperty("user_plan_context") UserPlanContext userPlanContext,
        @JsonProperty("calendar") Calendar calendar) {

    public static DashboardContract emptyState(DateRange dateRange) {
        return new DashboardContract(
                new UserPlanContext(UserPlanContext.PlanStatus.NOT_CONFIGURED, null),
                Calendar.empty(dateRange)
        );
    }

    //
    public record UserPlanContext(
            @JsonProperty("status") PlanStatus status,
            @JsonProperty("plan_id") String planId //nullable
    ) {
        public enum PlanStatus {
            ACTIVE,
            NOT_CONFIGURED
        }
    }
}
