package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract;

public record TrainingSessionDayDocument(
        @JsonProperty(value = "id")
        String sessionId,
        @JsonProperty(value = "name")
        String sessionName,
        @JsonProperty(value = "plan_name")
        String planName,
        @JsonProperty(value = "day_meta_data")
        DayMetaData dayMetaData,
        @JsonProperty(value = "day_to_configuration_context")
        DayToConfigurationContext dayToConfigurationContext
) {
    public record DayMetaData(

            @JsonProperty(value = "day_type")
            UserDashboardContract.DayState.DayType dayType, //TODO move from day state
            @JsonProperty(value = "day_state")
            DayState dayState) {
        public enum DayState {
            TODO, ONGOING, FINISHED, SHIPPED
        }
    }

    public record DayToConfigurationContext(
            @JsonProperty(value = "configured_days_amount")
            int allParts,
            @JsonProperty(value = "day_number")
            int partNumber) {
    }
}
