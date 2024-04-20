package com.shapyfy.core.boundary.api.exercise;

import com.shapyfy.core.boundary.api.exercise.adapter.ExerciseApiAdapter;
import com.shapyfy.core.boundary.api.exercise.model.CreateExerciseRequest;
import com.shapyfy.core.boundary.api.exercise.model.ExerciseContract;
import com.shapyfy.core.domain.model.Exercise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseApiAdapter exerciseApiAdapter;

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ExerciseContract> getExercise(@PathVariable(name = "exerciseId") UUID exerciseId) {
        log.debug("Fetching exercise with id: {}", exerciseId);
        return ResponseEntity.ok(exerciseApiAdapter.fetchExercise(exerciseId));
    }

    @GetMapping
    public ResponseEntity<List<ExerciseContract>> getExercises() {
        log.debug("Fetching all exercises");
        return ResponseEntity.ok(exerciseApiAdapter.fetchExercises());
    }

    @PostMapping
    public ResponseEntity<ExerciseContract> createExercise(@RequestBody CreateExerciseRequest request) {
        log.info("Creating exercise: {}", request);
        Exercise createdExercise = exerciseApiAdapter.createExercise(request);
        log.info("Created exercise: {}", createdExercise);

        return ResponseEntity.ok(ExerciseContract.from(createdExercise));
    }
}
