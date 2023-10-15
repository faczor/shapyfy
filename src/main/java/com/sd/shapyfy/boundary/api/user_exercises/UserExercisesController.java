package com.sd.shapyfy.boundary.api.user_exercises;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@RequiredArgsConstructor
@ApiV1("/v1/user_exercises")
public class UserExercisesController {

    @GetMapping("/{exercise_id}/statistics")
    public ResponseEntity<Void> getExerciseStatistics(@PathVariable("exercise_id") UUID pathExerciseId) {
        UserId userId = currentUserId();
        ExerciseId exerciseId = ExerciseId.of(pathExerciseId);
        log.info("Attempt to fetch exercise history {} {}", userId, exerciseId);

        return ResponseEntity.ok().build();
    }
}
