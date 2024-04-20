package com.shapyfy.core.boundary.api.training.adapter;

import com.shapyfy.core.boundary.api.training.model.ConfigurePlanRequest;
import com.shapyfy.core.domain.Exercises;
import com.shapyfy.core.domain.TrainingPlanCreator;
import com.shapyfy.core.domain.TrainingPlanCreator.CreateTrainingPlanRequest.CreatePlanDayRequest;
import com.shapyfy.core.domain.TrainingPlanCreator.CreateTrainingPlanRequest.CreatePlanDayRequest.CreateWorkoutExerciseConfigRequest;
import com.shapyfy.core.domain.model.Exercise;
import com.shapyfy.core.domain.model.PlanDayType;
import com.shapyfy.core.domain.model.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.shapyfy.core.util.StreamUtils.ofNullable;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingApiAdapter {

    private final TrainingPlanCreator trainingPlanCreator;

    private final Exercises exercises;

    public void createTraining(ConfigurePlanRequest request, UserId userId) {
        log.info("Creating training plan {} by {}", request, userId);
        trainingPlanCreator.create(
                new TrainingPlanCreator.CreateTrainingPlanRequest(
                         request.name(),
                        request.startDate(),
                        request.configureDays().stream().map(dayConfig -> new CreatePlanDayRequest(
                                dayConfig.type(),
                                dayConfig.type() == PlanDayType.REST_DAY ? "REST_DAY" : dayConfig.name(),
                                ofNullable(dayConfig.requests()).map(exerciseConfig -> new CreateWorkoutExerciseConfigRequest(
                                        exercises.fetchById(Exercise.ExerciseId.of(exerciseConfig.id())),
                                        exerciseConfig.weight(),
                                        exerciseConfig.sets(),
                                        exerciseConfig.reps(),
                                        exerciseConfig.restTime(),
                                        dayConfig.requests().indexOf(exerciseConfig)
                                )).toList()
                        )).toList()
                ), userId);
    }
}
