package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.domain.configuration.ConfigurationService;
import com.sd.shapyfy.domain.configuration.ConfigurationService.EditableSessionParams.SessionExerciseExerciseEditableParam;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.component.PostgresExerciseFetcher;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayNotFound;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.model.ExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.component.PostgresTrainingDayRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter.TrainingDayToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.model.TrainingDayEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresSessionService implements ConfigurationService {

    private final PostgresSessionRepository sessionRepository;

    private final PostgresTrainingDayRepository trainingDayRepository;

    private final TrainingDayToDomainConverter trainingDayToDomainConverter;

    private final PostgresExerciseFetcher exerciseFetcher;

    @Override
    public ConfigurationDay createSession(ConfigurationDayId configurationDayId, EditableSessionParams editableSessionParams) {
        log.info("Create training day {} with params {}", configurationDayId, editableSessionParams);
        TrainingDayEntity trainingDayEntity = getById(configurationDayId);
        trainingDayEntity.createNewSession(buildUpdateSessionData(editableSessionParams));
        TrainingDayEntity trainingDay = trainingDayRepository.save(trainingDayEntity);

        return trainingDayToDomainConverter.toConfiguration(trainingDay);
    }

    @Override
    public void updateSessionWithState(ConfigurationDayId configurationDayId, SessionState state, EditableSessionParams editableSessionParams) {
        log.info("Update training day {} with session state {} with params {}", configurationDayId, state, editableSessionParams);
        TrainingDayEntity trainingDayEntity = getById(configurationDayId);
        SessionEntity sessionToUpdate = trainingDayEntity.sessionWithState(state);
        sessionToUpdate.update(buildUpdateSessionData(editableSessionParams));
        sessionRepository.save(sessionToUpdate);
    }


    private TrainingDayEntity getById(ConfigurationDayId configurationDayId) {
        return trainingDayRepository.findById(configurationDayId.getValue()).orElseThrow(() -> new TrainingDayNotFound("Training day not found "));
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
