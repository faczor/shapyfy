package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.boundary.event.OnExerciseFinishEvent;
import com.sd.shapyfy.domain.EventPublisher;
import com.sd.shapyfy.domain.configuration.model.AttributeId;
import com.sd.shapyfy.domain.configuration.model.ExerciseSetId;
import com.sd.shapyfy.domain.configuration.model.TrainingExerciseId;
import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.exercise.TrainingExerciseLookup;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingProcess {

    private final TrainingExerciseLookup trainingExerciseLookup;

    private final TrainingExerciseService trainingExerciseService;

    private final EventPublisher eventPublisher;

    //TODO mark as training active?
    public SessionExerciseWithPreviousOccurrences runExercise(SessionPartId sessionPartId, ExerciseId exerciseId, UserId userId) {
        log.info("Run exercise {} on {} for {}", exerciseId, sessionPartId, userId);
        TrainingExercise trainingExercise = trainingExerciseLookup.lookup(sessionPartId, exerciseId);
        List<TrainingExercise> finishedExerciseOccurrences = trainingExerciseLookup.lookupAllFinished(exerciseId, userId);
        return new SessionExerciseWithPreviousOccurrences(
                trainingExercise,
                finishedExerciseOccurrences);
    }

    public TrainingExercise finishExercise(SessionId sessionId, SessionPartId sessionPartId, UserId userId, UpdateTrainingExercise updateTrainingExercise) {
        log.info("Finish exercise {}", updateTrainingExercise);
        TrainingExercise updatedExercise = trainingExerciseService.update(updateTrainingExercise);
        eventPublisher.publish(new OnExerciseFinishEvent(this, updatedExercise, userId, sessionId,sessionPartId));
        return updatedExercise;
    }

    public record SessionExerciseWithPreviousOccurrences(
            TrainingExercise trainingExercise,
            List<TrainingExercise> finishedExerciseOccurrences) {
    }

    public record UpdateTrainingExercise(
            TrainingExerciseId id,
            boolean isFinished,
            List<UpdateAttributeRequest> updateAttributeRequests,
            List<UpdateSetRequest> updateSetRequests) {

        public record UpdateAttributeRequest(
                AttributeId id,
                String value) {
        }

        public record UpdateSetRequest(
                ExerciseSetId id,
                int reps,
                Double weight,
                boolean isFinished,
                List<UpdateAttributeRequest> updateAttributeRequests) {
        }
    }
}
