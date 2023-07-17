package com.sd.shapyfy.infrastructure.services.postgres.trainingDay;

import com.sd.shapyfy.domain.TrainingManagementAdapter;
import com.sd.shapyfy.domain.model.TrainingDay;
import com.sd.shapyfy.domain.model.TrainingDayId;
import com.sd.shapyfy.domain.model.TrainingDayType;
import com.sd.shapyfy.domain.TrainingDaysPort;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.ExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.PostgresExercisePort;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.PostgresSessionRepository;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.SessionExerciseEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter.TrainingDayEntityToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresTrainingDayPort implements TrainingDaysPort {

    private final PostgresTrainingDayRepository trainingDayRepository;

    private final PostgresExercisePort exercisePort;

    private final TrainingDayEntityToDomainConverter trainingDayEntityToDomainConverter;

    private final PostgresSessionRepository sessionRepository;

    private final PostgresSessionExerciseRepository sessionExerciseRepository;

    //TODO Fix returnal (setting and adding to saved entities)
    @Override
    public TrainingDay selectExercises(TrainingDayId trainingDayId, List<TrainingManagementAdapter.SelectedExercise> selectedExerciseDetails) {
        TrainingDayEntity trainingDayEntity = trainingDayRepository.findById(trainingDayId.getValue()).orElseThrow(() -> new TrainingDayNotFound("Not found resource " + trainingDayId));
        SessionEntity session = Optional.ofNullable(trainingDayEntity.getSessions()).filter(CollectionUtils::isNotEmpty).map(sessions -> sessions.get(0)).orElse(SessionEntity.init(trainingDayEntity));

        List<SessionExerciseEntity> sessionExercises = selectedExerciseDetails.stream().map(selectedExercise -> buildSessionExercises(selectedExercise, session)).toList();

        SessionEntity savedSession = sessionRepository.save(session);
        List<SessionExerciseEntity> savedExercises = sessionExerciseRepository.saveAll(sessionExercises);
        savedSession.setSessionExercises(savedExercises);

        trainingDayEntity.getSessions().add(savedSession);
        return trainingDayEntityToDomainConverter.convert(trainingDayEntity);
    }

    private SessionExerciseEntity buildSessionExercises(TrainingManagementAdapter.SelectedExercise selectedExercise, SessionEntity session) {
        ExerciseEntity exercise = exercisePort.fetchFor(selectedExercise.getExerciseId());

        return new SessionExerciseEntity(
                null,
                selectedExercise.getSets(),
                selectedExercise.getReps(),
                selectedExercise.getWeight().orElse(null),
                false,
                exercise,
                session
        );
    }

    public List<TrainingDayEntity> createDays(TrainingEntity training, List<TrainingDay> trainingDays) {
        return trainingDays.stream().map(day -> new TrainingDayEntity(
                null,
                Optional.ofNullable(day.getName()).orElse("REST_DAY"),
                day.getDay(),
                day.getDayType() == TrainingDayType.OFF,
                training,
                List.of()
        )).map(trainingDayRepository::save).toList();
    }
}
