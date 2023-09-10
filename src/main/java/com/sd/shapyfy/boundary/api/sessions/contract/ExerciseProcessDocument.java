package com.sd.shapyfy.boundary.api.sessions.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.boundary.api.exercises.contract.ExerciseDocument;
import com.sd.shapyfy.boundary.api.plans.contract.TimeAmountDocument;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExerciseProcessDocument(

        @JsonProperty(value = "id")
        UUID id,
        @JsonProperty(value = "exercise")
        ExerciseDocument exercise,
        @JsonProperty(value = "break_between_sets")
        TimeAmountDocument breakBetweenSets,
        @JsonProperty(value = "is_finished")
        boolean isFinished,
        @JsonProperty(value = "attributes")
        List<AttributeDocument> attributes,
        @JsonProperty(value = "sets")
        List<SetDocument> sets,
        @JsonProperty(value = "history")
        List<ExerciseHistory> history
) {
    public record ExerciseHistory(
            @JsonProperty(value = "date")
            LocalDate date,
            @JsonProperty(value = "set_histories")
            List<SetHistory> setHistories) {
        public record SetHistory(
                @JsonProperty(value = "reps")
                int reps,
                @JsonProperty(value = "weight")
                double weight,
                @JsonProperty(value = "attributes")
                List<AttributeDocument> attributes) {
        }
    }
}
