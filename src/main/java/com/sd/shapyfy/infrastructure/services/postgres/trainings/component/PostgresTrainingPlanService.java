package com.sd.shapyfy.infrastructure.services.postgres.trainings.component;

import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanService;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.component.PostgresExerciseFetcher;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.component.UpdateSessionPartData;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.PlanConfigurationToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresTrainingPlanService implements TrainingPlanService {

    private final PostgresTrainingRepository trainingRepository;

    private final PlanConfigurationToDomainConverter planConfigurationToDomainConverter;

    private final PostgresExerciseFetcher exerciseFetcher;

    @Override
    public TrainingConfiguration create(PlanConfiguration configurationParams, UserId userId) {
        log.info("Attempt to create training plan for user {} with configuration {}", userId, configurationParams);
        TrainingEntity training = TrainingEntity.create(configurationParams.name(), userId);
        SessionEntity newSession = training.createNewSession();
        configurationParams.sessionDayConfigurations().forEach(sessionDayConfiguration -> {
            UpdateSessionPartData updateSessionPartData = buildUpdateSessionPartData(sessionDayConfiguration);
            newSession.createPart(updateSessionPartData.name(), updateSessionPartData.type())
                    .update(updateSessionPartData);
        });

        trainingRepository.save(training);

        return planConfigurationToDomainConverter.convert(training);
    }

    //TODO proper exception
    public TrainingEntity findById(PlanId planId) {
        return trainingRepository.findById(planId.getValue())
                .orElseThrow();
    }

    public TrainingEntity save(TrainingEntity training) {
        return trainingRepository.save(training);
    }

    private UpdateSessionPartData buildUpdateSessionPartData(PlanConfiguration.SessionDayConfiguration sessionDayConfiguration) {
        return new UpdateSessionPartData(
                sessionDayConfiguration.dayType(),
                null,
                sessionDayConfiguration.name(),
                Optional.ofNullable(sessionDayConfiguration.selectedExercises()).map(this::buildUpdateExerciseData).orElse(null)
        );
    }

    private List<UpdateSessionPartData.UpdateExercise> buildUpdateExerciseData(List<PlanConfiguration.SessionDayConfiguration.SelectedExercise> selectedExercises) {
        return selectedExercises.stream().map(selectedExercise -> new UpdateSessionPartData.UpdateExercise(
                exerciseFetcher.fetchFor(selectedExercise.exerciseId()),
                selectedExercise.sets(),
                selectedExercise.reps(),
                selectedExercise.weight(),
                selectedExercise.secondRestBetweenSets(),
                false
        )).toList();
    }
}
