package com.sd.shapyfy.boundary.api.exercises;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.exercises.contract.ExercisesListingDocument;
import com.sd.shapyfy.domain.ExerciseAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@ApiV1("/v1/exercises")
public class ExercisesController {

    private final ExerciseAdapter exerciseAdapter;

    @GetMapping
    public ResponseEntity<ExercisesListingDocument> listExercises() {
        ExercisesListingDocument records = ExercisesListingDocument.from(exerciseAdapter.fetchExercises());

        return ResponseEntity.ok(records);
    }
}
