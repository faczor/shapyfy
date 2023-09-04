package com.sd.shapyfy.boundary.api.plans.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TimeAmountDocument(
        @NotNull
        @Min(value = 0)
        @JsonProperty(value = "minutes", required = true)
        int minutes,
        //
        @NotNull
        @Min(value = 0)
        @Max(value = 59)
        @JsonProperty(value = "seconds", required = true)
        int seconds) {

    public int toSeconds() {
        int minutesToSeconds = minutes * 60;
        return seconds + minutesToSeconds;
    }

    public static TimeAmountDocument fromSeconds(int seconds) {
        return new TimeAmountDocument(
                seconds / 60,
                seconds % 60
        );
    }
}
