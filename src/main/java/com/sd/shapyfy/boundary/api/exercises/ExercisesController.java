package com.sd.shapyfy.boundary.api.exercises;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.exercises.contract.ExerciseDocument;
import com.sd.shapyfy.boundary.api.exercises.contract.ExercisesListingDocument;
import com.sd.shapyfy.domain.exercise.ExerciseCreator;
import com.sd.shapyfy.domain.exercise.ExerciseLookup;
import com.sd.shapyfy.domain.exercise.model.Exercise;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;
import static com.sd.shapyfy.boundary.api.exercises.Creator.COMMUNITY;

@Slf4j
@RequiredArgsConstructor
@ApiV1("/v1/exercises")
public class ExercisesController {

    private final ExerciseLookup exerciseLookup;

    private final ExerciseCreator exerciseCreator;

    @GetMapping
    public ResponseEntity<ExercisesListingDocument> listExercises() {
        ExercisesListingDocument records = ExercisesListingDocument.from(exerciseLookup.fetchExercises());

        return ResponseEntity.ok(records);
    }

    @PostMapping
    public ResponseEntity<ExerciseDocument> createExercise(@Valid @RequestBody CreateExerciseDocument createExerciseDocument) {
        log.info("User {} attempts to create examination {}", currentUserId(), createExerciseDocument.name());
        Exercise exercise = exerciseCreator.create(createExerciseDocument.name(), COMMUNITY);

        return ResponseEntity.ok(ExerciseDocument.from(exercise));
    }
}
