package com.shapyfy.core.boundary.api.trainingDay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shapyfy.core.domain.model.PlanDayType;
import com.shapyfy.core.domain.model.WorkoutExerciseConfig;
import com.shapyfy.core.domain.model.WorkoutSet;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PlanDayContract(
        //
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name,
        @JsonProperty("type") PlanDayType type,
        @JsonProperty("exercises") List<WorkoutExerciseContract> exercises
) {
    public record WorkoutExerciseContract(
            //
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name,
            @JsonProperty("config") ExerciseConfigContract config,
            @JsonProperty("history") List<PreviousWorkouts> previousWorkouts
    ) {
        public record ExerciseConfigContract(
                //
                @JsonProperty("reps") int reps,
                @JsonProperty("weight") double weight,
                @JsonProperty("sets") int sets,
                @JsonProperty("rest_time") int restTime) {

            public static ExerciseConfigContract from(WorkoutExerciseConfig config) {
                return new ExerciseConfigContract(config.getReps(), config.getWeight(), config.getSets(), config.getRestTime());
            }
        }

        public record PreviousWorkouts(
                //
                @JsonProperty("date") LocalDate date,
                @JsonProperty("sets") List<WorkoutSetContract> sets
        ) {

            public static PreviousWorkouts from(LocalDate date, List<WorkoutSet> workoutSets) {
                return new PreviousWorkouts(date, workoutSets.stream().map(WorkoutSetContract::from).toList());
            }

            public record WorkoutSetContract(
                    //
                    @JsonProperty("reps") int reps,
                    @JsonProperty("weight") double weight) {

                public static WorkoutSetContract from(WorkoutSet set) {
                    return new WorkoutSetContract(set.getReps(), set.getWeight());
                }
            }
        }
    }
}
