package com.sd.shapyfy.domain.exercise;

import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingExerciseLookup {

    private final TrainingExerciseFetcher trainingExerciseFetcher;

    public TrainingExercise lookup(SessionPartId sessionPartId, ExerciseId exerciseId) {
        log.info("Lookup {} {}", exerciseId, sessionPartId);
        return trainingExerciseFetcher.fetchFor(sessionPartId, exerciseId);
    }

    public List<TrainingExercise> lookupAllFinished(ExerciseId exerciseId, UserId userId) {
        log.info("Lookup history {} {}", exerciseId, userId);
        return trainingExerciseFetcher.fetchAllFinished(exerciseId, userId);
    }
}
