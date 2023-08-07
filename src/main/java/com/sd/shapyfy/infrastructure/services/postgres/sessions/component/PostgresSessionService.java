package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.domain.configuration.ConfigurationService;
import com.sd.shapyfy.domain.configuration.ConfigurationService.EditParams.SessionExerciseExerciseEditableParam;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.component.PostgresExerciseFetcher;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayNotFound;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.component.PostgresTrainingDayRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter.TrainingDayToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.PostgresTrainingPlanService;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresSessionService implements ConfigurationService {

    //TODO fixup dependencies ;)
    private final PostgresSessionPartRepository sessionPartRepository;

    private final PostgresTrainingDayRepository trainingDayRepository;

    private final TrainingDayToDomainConverter trainingDayToDomainConverter;

    private final PostgresExerciseFetcher exerciseFetcher;

    private final PostgresSessionRepository sessionRepository;

    private final PostgresTrainingPlanService trainingPlanService;

    @Override
    public void createSession(PlanId planId, List<EditParams> editParams) {
        log.info("Create session for {} with params {}", planId, editParams);
        TrainingEntity training = trainingPlanService.findById(planId);
        SessionEntity newSession = sessionRepository.save(training.createNewSession());

        editParams.forEach(param -> {
            TrainingDayEntity trainingDay = getById(param.configurationDayId());
            trainingDay.createNewSessionPart(newSession, buildUpdateSessionData(param));
        });

        trainingPlanService.save(training);
    }

    @Override
    public ConfigurationDay updateOrCreateFutureSessionConfiguration(EditTargetQuery query, EditParams editParams) {
        TrainingDayEntity trainingDayEntity = getById(query.configurationDayId());
        TrainingEntity training = trainingDayEntity.getTraining();
        //TODO Move validation to sql query above
        if (!Objects.equals(training.getId(), query.planId().getValue())) {
            throw new IllegalStateException();
        }

        SessionEntity sessionToUpdate = training.findSessionWithState(query.state()).orElseGet(() -> sessionRepository.save(training.createNewSession()));
        SessionPartEntity sessionPartToUpdate = sessionToUpdate.findSessionWithState(trainingDayEntity, query.state()).orElseGet(() -> sessionToUpdate.createPart(query.state(), trainingDayEntity));

        sessionPartToUpdate.update(buildUpdateSessionData(editParams));
        TrainingDayEntity trainingDay = trainingDayRepository.save(trainingDayEntity);

        return trainingDayToDomainConverter.toConfiguration(trainingDay);
    }



    private TrainingDayEntity getById(ConfigurationDayId configurationDayId) {
        return trainingDayRepository.findById(configurationDayId.getValue()).orElseThrow(() -> new TrainingDayNotFound("Training day not found "));
    }

    private UpdateSessionData buildUpdateSessionData(EditParams editParams) {
        return new UpdateSessionData(
                editParams.state(),
                editParams.date(),
                Optional.ofNullable(editParams.sessionExerciseExerciseEditableParam()).map(this::buildUpdateExerciseData).orElse(null)
        );
    }

    private List<UpdateSessionData.UpdateExercise> buildUpdateExerciseData(List<SessionExerciseExerciseEditableParam> sessionExerciseExerciseEditableParams) {
        return sessionExerciseExerciseEditableParams.stream().map(editableParams -> new UpdateSessionData.UpdateExercise(
                exerciseFetcher.fetchFor(editableParams.exerciseId()),
                editableParams.setsAmount(),
                editableParams.repsAmount(),
                editableParams.weightAmount(),
                editableParams.isFinished()
        )).toList();
    }

    public record UpdateSessionData(
            SessionState state,
            LocalDate date,
            List<UpdateExercise> updateExercises) {

        public record UpdateExercise(
                ExerciseEntity exercise,
                Integer setsAmount,
                Integer repsAmount,
                Double weightAmount,
                Boolean isFinished) {
        }
    }
}
