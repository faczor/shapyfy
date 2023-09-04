package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.configuration.model.ConfiguredExercises;
import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.exercise.TrainingExerciseLookup;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingDayProcess {

    private final TrainingExerciseLookup trainingExerciseLookup;

    //TODO mark as training active?
    public SessionExerciseWithPreviousOccurrences runExercise(SessionPartId sessionPartId, ExerciseId exerciseId, UserId userId) {
        log.info("Run exercise {} on {} for {}", exerciseId, sessionPartId, userId);
        TrainingExercise trainingExercise = trainingExerciseLookup.lookup(sessionPartId, exerciseId);
        List<TrainingExercise> finishedExerciseOccurrences = trainingExerciseLookup.lookupAllFinished(exerciseId, userId);
        return new SessionExerciseWithPreviousOccurrences(
                trainingExercise,
                finishedExerciseOccurrences);
    }

    public record SessionExerciseWithPreviousOccurrences(
            TrainingExercise trainingExercise,
            List<TrainingExercise> finishedExerciseOccurrences) {
    }
}
