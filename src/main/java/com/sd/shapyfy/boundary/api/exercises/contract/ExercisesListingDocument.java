package com.sd.shapyfy.boundary.api.exercises.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sd.shapyfy.domain.exercises.Exercise;

import java.util.List;

public record ExercisesListingDocument(
        @JsonProperty(value = "items", required = true) List<ExerciseDocument> exercises) {

    public static ExercisesListingDocument from(List<Exercise> exercises) {
        return new ExercisesListingDocument(
                exercises.stream().map(ExerciseDocument::from).toList());
    }
}
