package com.sd.shapyfy.infrastructure.services.postgres.v2;

import com.sd.shapyfy.domain.SessionService;
import com.sd.shapyfy.domain.SessionService.EditableSessionParams.SessionExerciseExerciseEditableParam;
import com.sd.shapyfy.domain.model.ExerciseId;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;
import com.sd.shapyfy.domain.session.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.ExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.PostgresExerciseRepository;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.PostgresSessionRepository;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.PostgresTrainingDayRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//TODO Rename name and service
@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresqlSessionService implements SessionService {

    private final PostgresSessionRepository sessionRepository;

    private final PostgresTrainingDayRepository trainingDayRepository;

    private final PostgresExerciseRepository postgresExerciseRepository;

    @Override
    public void createSession(ConfigurationDayId configurationDayId, EditableSessionParams editableSessionParams) {
        log.info("Create training day {} with params {}", configurationDayId, editableSessionParams);
        TrainingDayEntity trainingDayEntity = getById(configurationDayId);
        trainingDayEntity.createNewSession(buildUpdateSessionData(editableSessionParams));
        trainingDayRepository.save(trainingDayEntity);
    }

    @Override
    public void updateSessionWithState(ConfigurationDayId configurationDayId, SessionState state, EditableSessionParams editableSessionParams) {
        log.info("Update training day {} with session state {} with params {}", configurationDayId, state, editableSessionParams);
        TrainingDayEntity trainingDayEntity = getById(configurationDayId);
        SessionEntity sessionToUpdate = trainingDayEntity.sessionWithState(state);
        sessionToUpdate.update(buildUpdateSessionData(editableSessionParams));
        sessionRepository.save(sessionToUpdate);
    }


    //TODO proper exception
    private TrainingDayEntity getById(ConfigurationDayId configurationDayId) {
        return trainingDayRepository.findById(configurationDayId.getValue()).orElseThrow();
    }

    //TODO proper exception
    private ExerciseEntity getById(ExerciseId exerciseId) {
        return postgresExerciseRepository.findById(exerciseId.getValue()).orElseThrow();
    }

    private UpdateSessionData buildUpdateSessionData(EditableSessionParams editableSessionParams) {
        return new UpdateSessionData(
                editableSessionParams.state(),
                editableSessionParams.date(),
                Optional.ofNullable(editableSessionParams.sessionExerciseExerciseEditableParam()).map(this::buildUpdateExerciseData).orElse(null)
        );
    }

    private List<UpdateSessionData.UpdateExercise> buildUpdateExerciseData(List<SessionExerciseExerciseEditableParam> sessionExerciseExerciseEditableParams) {
        return sessionExerciseExerciseEditableParams.stream().map(editableParams -> new UpdateSessionData.UpdateExercise(
                getById(editableParams.exerciseId()),
                editableParams.setsAmount(),
                editableParams.repsAmount(),
                editableParams.weightAmount(),
                editableParams.isFinished()
        )).toList();
    }

    public record UpdateSessionData(
            SessionState state,
            LocalDate date,
            List<UpdateExercise> updateExercises
    ) {

        public record UpdateExercise(
                ExerciseEntity exercise,
                Integer setsAmount,
                Integer repsAmount,
                Double weightAmount,
                Boolean isFinished) {
        }
    }
}
