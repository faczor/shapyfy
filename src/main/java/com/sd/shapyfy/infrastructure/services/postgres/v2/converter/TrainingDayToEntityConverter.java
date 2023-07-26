package com.sd.shapyfy.infrastructure.services.postgres.v2.converter;

import com.sd.shapyfy.domain.model.ConfigurationDayType;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator;
import com.sd.shapyfy.infrastructure.services.postgres.trainingDay.TrainingDayEntity;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrainingDayToEntityConverter {

    public TrainingDayEntity onInitialization(TrainingPlanCreator.PlanCreationInitialConfigurationParams.SessionDayConfiguration sessionDayConfiguration, TrainingEntity training) {
        return new TrainingDayEntity(
                null,
                Optional.ofNullable(sessionDayConfiguration.name()).orElse("REST_DAY"),
                DayOfWeek.MONDAY, //TODO Clean up
                sessionDayConfiguration.dayType() == ConfigurationDayType.REST,
                training,
                List.of()
        );
    }
}
