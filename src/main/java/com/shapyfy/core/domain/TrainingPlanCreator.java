package com.shapyfy.core.domain;

import com.shapyfy.core.domain.legacy.configuration.model.ConfigurationAttributeType;
import com.shapyfy.core.domain.legacy.configuration.model.PlanDayType;
import com.shapyfy.core.domain.model.Exercise;
import com.shapyfy.core.domain.model.PlanDayType;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.domain.model.UserId;
import com.shapyfy.core.domain.port.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingPlanCreator {

    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingPlan create(CreateConfigurationRequest createConfigurationRequest, UserId userId) {
        log.info("Creating training plan {} by {}", createConfigurationRequest, userId);
        var createdPlan = trainingPlanRepository.save(TrainingPlan.from(createConfigurationRequest, userId));
        log.info("Training plan created {}", createdPlan);
        return createdPlan;
    }

    public record CreateConfigurationRequest(
            String name,
            List<CreateConfigurationDayRequest> createConfigurationDayRequests) {
        public record CreateConfigurationDayRequest(
                PlanDayType type,
                String name,
                List<CreateExerciseConfigurationRequest> createExerciseConfigurationRequests) {
            public record CreateExerciseConfigurationRequest(
                    Exercise.ExerciseId id,
                    double weight,
                    int sets,
                    int reps) {
            }
        }
    }
}
