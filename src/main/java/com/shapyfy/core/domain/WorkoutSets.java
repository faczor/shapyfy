package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.WorkoutExerciseConfig;
import com.shapyfy.core.domain.port.WorkoutExerciseRepository;
import com.shapyfy.core.domain.port.WorkoutSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkoutSets {

    private final WorkoutSetRepository workoutSetRepository;

    private final WorkoutExerciseRepository workoutExerciseRepository;

    public void create(WorkoutExerciseConfig.WorkoutExerciseConfigId id, CreateWorkoutSetParams params) {
        log.info("Attempt to create workout set for {}", id);

    }

    public record CreateWorkoutSetParams(
            int reps,
            double weight
    ){}
}
