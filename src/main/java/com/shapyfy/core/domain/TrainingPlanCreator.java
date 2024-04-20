package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.Exercise;
import com.shapyfy.core.domain.model.PlanDayType;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.domain.model.UserId;
import com.shapyfy.core.domain.port.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingPlanCreator {

    private final TrainingPlanRepository trainingPlanRepository;

    public TrainingPlan create(CreateTrainingPlanRequest request, UserId userId) {
        log.info("Creating training plan {} by {}", request, userId);
        var createdPlan = trainingPlanRepository.save(TrainingPlan.from(request, userId));
        log.info("Training plan created {}", createdPlan);
        return createdPlan;
    }

    public record CreateTrainingPlanRequest(
            //
            String name,
            LocalDate startDate,
            List<CreatePlanDayRequest> createPlan) {
        public record CreatePlanDayRequest(
                //
                PlanDayType type,
                String name,
                List<CreateWorkoutExerciseConfigRequest> createWorkoutExerciseConfigs) {
            public record CreateWorkoutExerciseConfigRequest(
                    //
                    Exercise exercise,
                    double weight,
                    int sets,
                    int reps,
                    int restTime,
                    int order) {
            }
        }
    }
}
