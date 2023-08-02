package com.sd.shapyfy.domain;

import com.google.common.collect.Iterables;
import com.sd.shapyfy.domain.plan.ConfigurationDay;
import com.sd.shapyfy.domain.plan.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.PlanConfiguration;
import com.sd.shapyfy.domain.plan.PlanId;
import com.sd.shapyfy.domain.session.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingPlanActivator {

    private final TrainingLookup trainingLookup;

    private final ConfigurationService configurationService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SessionService sessionService;

    public void activate(PlanId planId, ConfigurationDayId configurationDayId, LocalDate startDate) {
        log.info("Attempt to activate {} by {} on {}", planId, configurationDayId, startDate);
        PlanConfiguration planConfiguration = trainingLookup.configurationFor(planId);

        int indexOfConfigurationDay = findIndexOfConfigurationDay(planConfiguration.configurationDays(), configurationDayId);
        List<ActivateSession> sessionsToActive = sessionsToActivate(indexOfConfigurationDay, planConfiguration.configurationDays(), startDate);

        activateSessions(sessionsToActive);

        applicationEventPublisher.publishEvent(new OnTrainingActivationEvent(this, planConfiguration, Iterables.getLast(sessionsToActive).date()));
    }

    private void activateSessions(List<ActivateSession> activateSessions) {
        for (ActivateSession activateSession : activateSessions) {
            sessionService.updateSessionWithState(
                    activateSession.id(),
                    SessionState.DRAFT,
                    new SessionService.EditableSessionParams(
                            SessionState.ACTIVE,
                            activateSession.date(),
                            null
                    )
            );
        }
    }

    private List<ActivateSession> sessionsToActivate(int indexOfStartingDay, List<ConfigurationDay> configurationDays, LocalDate startDate) {
        List<ActivateSession> activateSessions = new ArrayList<>();
        for (int index = indexOfStartingDay; index < configurationDays.size(); index++) {
            ConfigurationDay configurationDay = configurationDays.get(index);
            if (configurationDay.isTrainingDay()) {
                activateSessions.add(new ActivateSession(configurationDay.id(), startDate.plusDays(index)));
            }
        }
        return activateSessions;
    }

    //TODO Throw proper exception :)
    private static int findIndexOfConfigurationDay(List<ConfigurationDay> configurationDays, ConfigurationDayId searchingConfigId) {
        for (int index = 0; index < configurationDays.size(); index++) {
            ConfigurationDay configurationDay = configurationDays.get(index);
            if (configurationDay.id().equals(searchingConfigId)) {
                return index;
            }
        }

        throw new IllegalStateException();
    }

    record ActivateSession(
            ConfigurationDayId id,
            LocalDate date
    ) {
    }
}
