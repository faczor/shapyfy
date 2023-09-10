package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.exercise.model.ExerciseId;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingPlanCreator {

    private final TrainingPlanService trainingPlanService;

    public TrainingConfiguration create(PlanConfiguration configurationParams, UserId userId) {
        log.info("Attempt to create training plan for user {} with configuration {}", userId, configurationParams);
        TrainingConfiguration trainingConfiguration = trainingPlanService.create(configurationParams, userId);
        log.info("Training plan created for user {} with configuration {}", userId, trainingConfiguration);
        return trainingConfiguration;
    }


    public record PlanConfiguration(
            String name,
            List<String> exerciseAttributes,
            List<String> setAttributes,
            List<SessionDayConfiguration> sessionDayConfigurations) {

        public record SessionDayConfiguration(
                String name,
                SessionPartType dayType,
                List<SelectedExercise> selectedExercises) {

          public   record SelectedExercise(
                    ExerciseId exerciseId,
                    int sets,
                    int reps,
                    Double weight,
                    int secondRestBetweenSets) {
            }
        }
    }
}
