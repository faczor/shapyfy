package com.sd.shapyfy.boundary.sessions;

import com.sd.shapyfy.boundary.sessions.contract.CreateSessionDocument;
import com.sd.shapyfy.boundary.sessions.contract.SessionAddExercisesDocument;
import com.sd.shapyfy.domain.training.TrainingId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/training/{training_id}/sessions")
public class TraineeTrainingSessionsController {

    @GetMapping
    public ResponseEntity<Object> listTrainingSessions(@PathVariable(name = "training_id") String id) {
        TrainingId trainingId = TrainingId.of(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> createTrainingSession(
            @PathVariable(name = "training_id") String id,
            @RequestBody @Valid CreateSessionDocument createSessionDocument) {
        TrainingId trainingId = TrainingId.of(id);

        return ResponseEntity.ok(null);
    }

    @PutMapping("/{session_id}")
    public ResponseEntity<Object> fillSession(
            @PathVariable(name = "training_id") String trainingId,
            @PathVariable(name = "session_id") String sessionId,
            @RequestBody @Valid SessionAddExercisesDocument sessionAddExercisesDocument) {

        return ResponseEntity.ok(null);
    }
}
