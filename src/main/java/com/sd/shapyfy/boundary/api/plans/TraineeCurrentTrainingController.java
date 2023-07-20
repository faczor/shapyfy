package com.sd.shapyfy.boundary.api.plans;

import com.sd.shapyfy.SystemTime;
import com.sd.shapyfy.boundary.api.ApiV1;
import com.sd.shapyfy.boundary.api.TokenUtils;
import com.sd.shapyfy.boundary.api.plans.contract.CurrentSessionDocument;
import com.sd.shapyfy.boundary.api.plans.contract.CurrentPlanResponseDocument;
import com.sd.shapyfy.boundary.api.plans.contract.ExerciseAttributesDocument;
import com.sd.shapyfy.domain.TrainingLookup;
import com.sd.shapyfy.domain.TrainingPort;
import com.sd.shapyfy.domain.model.Session;
import com.sd.shapyfy.domain.model.SessionExerciseId;
import com.sd.shapyfy.domain.model.SessionId;
import com.sd.shapyfy.domain.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@ApiV1("/v1/current-trainings")
@RequiredArgsConstructor
public class TraineeCurrentTrainingController {

    private final TrainingLookup trainingLookup;

    private final TrainingPort trainingPort;

    private final SystemTime systemTime;

    @GetMapping
    public ResponseEntity<CurrentPlanResponseDocument> trainingDetails() {
        UserId userId = TokenUtils.currentUserId();
        TrainingLookup.CurrentTraining training = trainingLookup.currentTrainingFor(userId);

        return ResponseEntity.ok(CurrentPlanResponseDocument.from(training));
    }

    @PatchMapping
    public ResponseEntity<CurrentSessionDocument> startTodaySession() {
        UserId userId = TokenUtils.currentUserId();

        Session session = trainingPort.runSession(userId, systemTime.today());

        return ResponseEntity.ok(CurrentSessionDocument.from(session));
    }

    @PatchMapping("/{exercise_id}/finish-exercises")
    public ResponseEntity<Void> finishExercise(
            @PathVariable("exercise_id") String exerciseId,
            @RequestBody ExerciseAttributesDocument document) {

        trainingPort.finishExercise(
                SessionExerciseId.of(exerciseId),
                new TrainingPort.ExerciseAttributes(document.weight()));

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{session_id}/finish-sessions")
    public ResponseEntity<Void> finishTrainingSession(
            @PathVariable("session_id") String sessionId) {

        trainingPort.finishTrainingSession(SessionId.of(sessionId));

        return ResponseEntity.ok().build();
    }
}
