package com.sd.shapyfy.boundary.api.exercises;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.exercises.contract.ExercisesListingDocument;
import com.sd.shapyfy.domain.exercise.ExerciseLookup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@ApiV1("/v1/exercises")
public class ExercisesController {

    private final ExerciseLookup exerciseLookup;

    @GetMapping
    public ResponseEntity<ExercisesListingDocument> listExercises() {
        ExercisesListingDocument records = ExercisesListingDocument.from(exerciseLookup.fetchExercises());

        return ResponseEntity.ok(records);
    }
}
