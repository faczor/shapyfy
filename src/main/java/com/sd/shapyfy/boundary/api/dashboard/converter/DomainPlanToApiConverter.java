package com.sd.shapyfy.boundary.api.dashboard.converter;

import com.google.common.collect.Iterables;
import com.sd.shapyfy.boundary.api.plans.contract.TrainingSessionDayDocument;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.model.Training;
import org.springframework.stereotype.Component;


import static com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract.DayState.DayType.REST_DAY;
import static com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract.DayState.DayType.TRAINING_DAY;
import static com.sd.shapyfy.boundary.api.plans.contract.TrainingSessionDayDocument.DayMetaData.DayState.*;

@Component
public class DomainPlanToApiConverter {

    public TrainingSessionDayDocument convert(Training training, SessionPart sessionPart, ConfigurationDayId configurationDayId) {

        ConfigurationDay configuration = configurationForPart(training, sessionPart);

        return new TrainingSessionDayDocument(
                training.sessions().stream().filter(s -> s.sessionParts().stream().anyMatch(p -> p.id().equals(sessionPart.id()))).findFirst().get().sessionId().getValue(),
                sessionPart.id().getValue(),
                configuration.name(),
                training.configuration().plan().name(),
                new TrainingSessionDayDocument.DayMetaData(
                        configuration.isTrainingDay() ? TRAINING_DAY : REST_DAY,
                        resolveDayState(sessionPart)
                ),
                new TrainingSessionDayDocument.DayToConfigurationContext(
                        training.configuration().configurationDays().size(),
                        Iterables.indexOf(training.configuration().configurationDays(), x-> x.id().equals(configurationDayId)) + 1
                )
        );
    }

    private TrainingSessionDayDocument.DayMetaData.DayState resolveDayState(SessionPart sessionPart) {
        if (sessionPart.isFinished()) {
            return FINISHED;
        } else if (sessionPart.isNotStarted()) {
            return TODO;
        } else {
            return ONGOING;
        }

    }

    private static ConfigurationDay configurationForPart(Training training, SessionPart sessionPart) {
        return training.configuration().forSessionPart(sessionPart);
    }
}
