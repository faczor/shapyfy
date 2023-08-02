package com.sd.shapyfy.infrastructure.services.postgres.v2;

import com.sd.shapyfy.domain.PlanExerciseSelector;
import com.sd.shapyfy.domain.ConfigurationService;
import com.sd.shapyfy.domain.plan.ConfigurationDay;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanCreationInitialConfigurationParams.SessionDayConfiguration;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.PostgresExercisePort;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.PostgresSessionRepository;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.PostgresTrainingDayRepository;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayNotFound;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import com.sd.shapyfy.infrastructure.services.postgres.v2.converter.SessionToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.v2.converter.TrainingDayToEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostgresqlTrainingDayService implements ConfigurationService {

    private final PostgresTrainingDayRepository trainingDayRepository;


    private final PostgresExercisePort exercisePort; //TODO change to exerciseService

    private final PostgresSessionRepository sessionRepository;

    private final TrainingDayToEntityConverter trainingDayToEntityConverter;

    private final SessionToDomainConverter sessionToDomainConverter;

    //Currently support only replacing
    @Override
    public ConfigurationDay fillConfigurationWithExercises(ConfigurationDayId configurationDayId, List<PlanExerciseSelector.SelectedExercise> selectedExercises) {
        TrainingDayEntity trainingDayEntity = findById(configurationDayId);

        SessionEntity sessionEntity = Optional.ofNullable(trainingDayEntity.getSessions()).filter(CollectionUtils::isNotEmpty).map($ -> $.get(0))
                .orElse(SessionEntity.init(trainingDayEntity));

        sessionEntity.setSessionExercises(selectedExercises.stream().map(exerciseParam -> convert(exerciseParam, sessionEntity)).toList());

        SessionEntity savedSession = sessionRepository.save(sessionEntity);

        return sessionToDomainConverter.convertToConfigurationDay(trainingDayEntity, savedSession);
    }

    public TrainingDayEntity initializeTrainingDay(SessionDayConfiguration sessionDayConfiguration, TrainingEntity training) {
        log.info("Attempt to initialize training day with configuration {}", sessionDayConfiguration);

        TrainingDayEntity savedDay = trainingDayRepository.save(trainingDayToEntityConverter.onInitialization(sessionDayConfiguration, training));
        log.info("Training day initialized with configuration {}", savedDay);
        return savedDay;
    }

    private TrainingDayEntity findById(ConfigurationDayId id) {
        return trainingDayRepository.findById(id.getValue())
                .orElseThrow(() -> new TrainingDayNotFound("Not found for " + id));
    }

    private SessionExerciseEntity convert(PlanExerciseSelector.SelectedExercise selectedExercise, SessionEntity session) {
        return SessionExerciseEntity.from(
                selectedExercise.sets(),
                selectedExercise.reps(),
                selectedExercise.weight(),
                exercisePort.fetchFor(selectedExercise.exerciseId()),
                session
        );
    }
}
