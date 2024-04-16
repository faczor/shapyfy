package com.shapyfy.core.domain.port;

import com.shapyfy.core.domain.model.WorkoutExerciseConfig;

public interface WorkoutExerciseRepository {

    WorkoutExerciseConfig findById(WorkoutExerciseConfig.WorkoutExerciseConfigId id);
}
