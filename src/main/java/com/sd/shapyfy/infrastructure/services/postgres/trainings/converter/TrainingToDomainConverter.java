package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.domain.DateRange;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.Training;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.ExistanceType;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter.TrainingDayToDomainConverter;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartType.TRAINING_DAY;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;

@Component
@RequiredArgsConstructor
public class TrainingToDomainConverter {

    private final TrainingDayToDomainConverter trainingDayToDomainConverter;

    private final PlanConfigurationToDomainConverter planConfigurationToDomainConverter;

    public Training convert(TrainingEntity training) {
        TrainingConfiguration configuration = planConfigurationToDomainConverter.convert(training);

        return new Training(
                planConfigurationToDomainConverter.convert(training),
                training.getSessions().stream().map(s -> toSessionConfiguration(configuration, s)).toList()
        );
    }

    //TODO move to proper converter :)
    public Session toSessionConfiguration(TrainingConfiguration configuration, SessionEntity sessionEntity) {
        List<SessionPartEntity> days = sessionEntity.getSessionParts().stream().filter(part -> part.getExistanceType() == ExistanceType.CONSTANT).toList();

        DateRange dateRange = sessionEntity.getState().haveTrainingDate()
                ? new DateRange(min(days, comparing(SessionPartEntity::getDate)).getDate(), max(days, comparing(SessionPartEntity::getDate)).getDate())
                : null;

        return new Session(
                SessionId.of(sessionEntity.getId()),
                sessionEntity.getState(),
                sessionEntity.getSessionParts().stream().map(sPart -> trainingDayToDomainConverter.toSession(configuration, sPart)).toList(),
                dateRange);

    }
}
