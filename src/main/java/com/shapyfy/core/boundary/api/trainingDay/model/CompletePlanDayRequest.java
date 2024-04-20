package com.shapyfy.core.boundary.api.trainingDay.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record CompletePlanDayRequest(
        //
        @JsonProperty("id") UUID planDayId,
        @JsonProperty("completed_exercises") List<CompletedExercise> completedExercises) {

    public record CompletedExercise(
            //
            @JsonProperty("id") UUID exerciseId,
            @JsonProperty("sets") List<CompletedSet> completedSets) {

        public record CompletedSet(
                //
                @JsonProperty("reps") int reps,
                @JsonProperty("weight") double weight) {
        }
    }
}
