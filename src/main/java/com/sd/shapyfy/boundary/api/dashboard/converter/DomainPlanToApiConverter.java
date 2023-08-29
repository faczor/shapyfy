package com.sd.shapyfy.boundary.api.dashboard.converter;

import com.google.common.collect.Iterables;
import com.sd.shapyfy.boundary.api.plans.contract.TrainingSessionDayDocument;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.plan.model.SessionPartId;
import com.sd.shapyfy.domain.plan.model.Training;
import org.springframework.stereotype.Component;

import static com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract.DayState.DayType.REST_DAY;
import static com.sd.shapyfy.boundary.api.dashboard.contact.UserDashboardContract.DayState.DayType.TRAINING_DAY;
import static com.sd.shapyfy.boundary.api.plans.contract.TrainingSessionDayDocument.DayMetaData.DayState.*;

@Component
public class DomainPlanToApiConverter {

    public TrainingSessionDayDocument convert(Training training, SessionPartId sessionPartId) {

        Session session = extractSessionFromTrainingContainingPart(training, sessionPartId);
        SessionPart sessionPart = session.partFor(sessionPartId);
        ConfigurationDay configuration = configurationForPart(training, sessionPart);

        return new TrainingSessionDayDocument(
                String.valueOf(sessionPartId.getValue()),
                configuration.name(),
                training.configuration().plan().name(),
                new TrainingSessionDayDocument.DayMetaData(
                        configuration.isTrainingDay() ? TRAINING_DAY : REST_DAY,
                        resolveDayState(sessionPart)
                ),
                new TrainingSessionDayDocument.DayToConfigurationContext(
                        training.configuration().configurationDays().size(),
                        Iterables.indexOf(training.configuration().configurationDays(), x-> x.id().equals(sessionPartId)) + 1
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

    private static Session extractSessionFromTrainingContainingPart(Training training, SessionPartId sessionPartId) {
        return training.sessions().stream()
                .filter(s -> s.sessionParts().stream().anyMatch(p -> p.configurationDayId().equals(sessionPartId)))
                .findFirst().orElseThrow();
    }

    private static ConfigurationDay configurationForPart(Training training, SessionPart sessionPart) {
        return training.configuration().configurationDays().stream().filter(c -> c.id().equals(sessionPart.configurationDayId())).findFirst().orElseThrow();
    }
}
