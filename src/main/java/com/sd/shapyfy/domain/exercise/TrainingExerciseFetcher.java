package com.sd.shapyfy.domain.exercise;

import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.plan.model.TrainingExercise;
import com.sd.shapyfy.domain.user.model.UserId;

import java.util.List;

public interface TrainingExerciseFetcher {
    TrainingExercise fetchFor(SessionPartId configurationDayId, ExerciseId exerciseId);

    List<TrainingExercise> fetchAllFinished(ExerciseId exerciseId, UserId userId);
}
