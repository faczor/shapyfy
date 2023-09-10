package com.sd.shapyfy.boundary.api.sessions;

import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.sessions.contract.CompleteExerciseDocument;
import com.sd.shapyfy.boundary.api.sessions.contract.ExerciseProcessDocument;
import com.sd.shapyfy.boundary.api.sessions.contract.SessionPartDocument;
import com.sd.shapyfy.boundary.api.sessions.contract.TrainingExerciseDocument;
import com.sd.shapyfy.boundary.api.sessions.converter.TrainingExerciseToDomainConverter;
import com.sd.shapyfy.boundary.api.sessions.converter.TrainingExerciseToApiConverter;
import com.sd.shapyfy.domain.configuration.model.TrainingExerciseId;
import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.TrainingProcess;
import com.sd.shapyfy.domain.plan.TrainingProcess.SessionExerciseWithPreviousOccurrences;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.domain.session.SessionLookup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import static com.sd.shapyfy.boundary.api.TokenUtils.currentUserId;

@Slf4j
@ApiV1("/v1/sessions")
@RequiredArgsConstructor
public class SessionsController {

    private final TrainingProcess trainingProcess;

    private final TrainingExerciseToApiConverter trainingExerciseToApiConverter;

    private final TrainingExerciseToDomainConverter trainingExerciseToDomainConverter;

    private final SessionLookup sessionLookup;

    @GetMapping("/{session_id}/part/{session_part_id}")
    public ResponseEntity<SessionPartDocument> getDay(
            @PathVariable(value = "session_id") UUID pathSessionId,
            @PathVariable(value = "session_part_id") UUID pathPartId) {
        log.info("Attempt to fetch day {} {}", pathSessionId, pathPartId);
        SessionPart sessionPart = sessionLookup.lookupPart(SessionId.of(pathSessionId), SessionPartId.of(pathPartId));

        return ResponseEntity.ok(SessionPartDocument.from(sessionPart));
    }

    //TODO Remove unused exercise
    @Deprecated(forRemoval = true)
    @PatchMapping("/{session_id}/part/{session_part_id}/exercise/{exercise_id}/starts")
    public ResponseEntity<ExerciseProcessDocument> runExercise(
            @PathVariable(value = "session_id") UUID pathSessionId,
            @PathVariable(value = "session_part_id") UUID pathPartId,
            @PathVariable(value = "exercise_id") UUID pathExerciseId) {
        log.info("Attempt to run exercise {} on session {} and part {}", pathExerciseId, pathSessionId, pathPartId);
        SessionExerciseWithPreviousOccurrences sessionExerciseWithPreviousOccurrences = trainingProcess.runExercise(SessionPartId.of(pathPartId), ExerciseId.of(pathExerciseId), currentUserId());

        return ResponseEntity.ok(trainingExerciseToApiConverter.convertOnRun(sessionExerciseWithPreviousOccurrences));
    }

    //TODO Add validation to ensure proper data.
    @PatchMapping("/{session_id}/part/{session_part_id}/exercise/{exercise_id}/completions")
    public ResponseEntity<TrainingExerciseDocument> exerciseCompletion(
            @PathVariable(value = "session_id") UUID pathSessionId,
            @PathVariable(value = "session_part_id") UUID pathPartId,
            @PathVariable(value = "exercise_id") UUID pathExerciseId,
            @RequestBody CompleteExerciseDocument document) {
        log.info("Attempt to finish exercise {} on session {} and part {}", pathExerciseId, pathSessionId, pathPartId);
        TrainingExercise trainingExercise = trainingProcess.finishExercise(
                SessionId.of(pathSessionId),
                SessionPartId.of(pathPartId),
                trainingExerciseToDomainConverter.convertForFinishExercise(TrainingExerciseId.of(pathExerciseId), document)
        );

        return ResponseEntity.ok(TrainingExerciseDocument.from(trainingExercise));
    }
}
