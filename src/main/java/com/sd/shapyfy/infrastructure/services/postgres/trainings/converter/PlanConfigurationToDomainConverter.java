package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.ExistanceType;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.model.TrainingEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.converter.TrainingDayToDomainConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanConfigurationToDomainConverter {

    private final TrainingPlanToDomainConverter trainingPlanToDomainConverter;

    private final TrainingDayToDomainConverter trainingDayToDomainConverter;

    public TrainingConfiguration convert(TrainingEntity trainingEntity) {
        SessionEntity session = trainingEntity.getConfigurationSession();

        return new TrainingConfiguration(
                trainingPlanToDomainConverter.convert(trainingEntity),
                SessionId.of(session.getId()),
                session.getSessionParts().stream().filter(part -> part.getExistanceType() == ExistanceType.CONSTANT).map(trainingDayToDomainConverter::toConfiguration).toList()
        );
    }

}
