package com.sd.shapyfy.boundary.api.training_day.contract;

import com.sd.shapyfy.boundary.api.exercises.contract.ExerciseDocument;
import com.sd.shapyfy.boundary.api.plans.contract.TimeAmountDocument;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExerciseProcessDocument(

        UUID id,
        ExerciseDocument exercise,
        TimeAmountDocument breakBetweenSets,
        boolean isFinished,
        List<AttributeDocument> attributes,
        List<SetDocument> sets,
        List<ExerciseHistory> history
) {
    public record ExerciseHistory(
            LocalDate date,
            List<SetHistory> setHistories) {
        public record SetHistory(
                int reps,
                double weight,
                List<AttributeDocument> attributes) {
        }
    }
}
