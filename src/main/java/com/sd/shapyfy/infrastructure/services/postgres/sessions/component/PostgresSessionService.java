package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.domain.configuration.SessionService;
import com.sd.shapyfy.domain.configuration.SessionService.EditSessionPartParams.SessionExerciseExerciseEditableParam;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.component.PostgresExerciseFetcher;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.PostgresTrainingPlanService;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.converter.TrainingToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresSessionService implements SessionService {

    private final TrainingToDomainConverter trainingToDomainConverter;

    private final PostgresExerciseFetcher exerciseFetcher;

    private final PostgresSessionRepository sessionRepository;

    private final PostgresTrainingPlanService trainingPlanService;

    @Override
    public void createSession(PlanId planId, EditSessionParams editSessionParams) {
        log.info("Create session for {} with params {}", planId, editSessionParams);
        TrainingEntity training = trainingPlanService.findById(planId);
        SessionEntity newSession = sessionRepository.save(training.createNewSession());
        newSession.update(editSessionParams.state());

        editSessionParams.editSessionPart().forEach(param -> {
            UpdateSessionPartData updateSessionPartData = buildUpdateSessionPartData(param.editSessionPartParams());
            newSession
                    .createPart(updateSessionPartData.name(), updateSessionPartData.type())
                    .update(updateSessionPartData);
        });

        trainingPlanService.save(training);
    }

    @Override
    public void updateSession(SessionId sessionId, EditSessionParams editSessionParams) {
        log.info("Update session for {} with params {}", sessionId, editSessionParams);
        SessionEntity sessionEntity = getById(sessionId);
        sessionEntity.update(editSessionParams.state());

        editSessionParams.editSessionPart().forEach(editSessionPart -> {
            SessionPartEntity sessionPart = sessionEntity.getSessionParts().stream()
                    .filter(part -> Objects.equals(part.getId(), editSessionPart.sessionPartId().getValue())).findFirst().orElseThrow();
            sessionPart.update(buildUpdateSessionPartData(editSessionPart.editSessionPartParams()));
        });

        sessionRepository.save(sessionEntity);
    }

    private SessionEntity getById(SessionId sessionId) {
        return sessionRepository.findById(sessionId.getValue())
                //TODO proper exception
                .orElseThrow();
    }

    private UpdateSessionPartData buildUpdateSessionPartData(EditSessionPartParams editSessionPartParams) {
        return new UpdateSessionPartData(
                editSessionPartParams.type(),
                editSessionPartParams.date(),
                null,
                Optional.ofNullable(editSessionPartParams.sessionExerciseExerciseEditableParam()).map(this::buildUpdateExerciseData).orElse(null)
        );
    }

    private List<UpdateSessionPartData.UpdateExercise> buildUpdateExerciseData(List<SessionExerciseExerciseEditableParam> sessionExerciseExerciseEditableParams) {
        return sessionExerciseExerciseEditableParams.stream().map(editableParams -> new UpdateSessionPartData.UpdateExercise(
                exerciseFetcher.fetchFor(editableParams.exerciseId()),
                editableParams.setsAmount(),
                editableParams.repsAmount(),
                editableParams.weightAmount(),
                editableParams.restBetweenSets(),
                editableParams.isFinished()
        )).toList();
    }

}
