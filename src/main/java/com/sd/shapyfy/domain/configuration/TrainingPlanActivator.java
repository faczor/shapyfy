package com.sd.shapyfy.domain.configuration;

import com.google.common.collect.Iterables;
import com.sd.shapyfy.domain.configuration.event.OnTrainingActivationEvent;
import com.sd.shapyfy.domain.configuration.exception.NotFoundDayInConfiguration;
import com.sd.shapyfy.domain.configuration.exception.TrainingNotConfiguredProperly;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDayId;
import com.sd.shapyfy.domain.configuration.model.PlanConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.isEmpty;
import static java.lang.String.format;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingPlanActivator {

    private final TrainingLookup trainingLookup;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ConfigurationService configurationService;

    public void activate(PlanId planId, ConfigurationDayId configurationDayId, LocalDate startDate) {
        log.info("Attempt to activate {} by {} on {}", planId, configurationDayId, startDate);
        PlanConfiguration planConfiguration = trainingLookup.configurationFor(planId);
        validateIfTrainingIsFilledProperly(planConfiguration.configurationDays());
        int indexOfConfigurationDay = findIndexOfConfigurationDay(planConfiguration.configurationDays(), configurationDayId);
        List<ActivateSession> sessionsToActive = sessionsToActivate(indexOfConfigurationDay, planConfiguration.configurationDays(), startDate);

        activateSessions(sessionsToActive);

        applicationEventPublisher.publishEvent(new OnTrainingActivationEvent(this, planConfiguration, Iterables.getLast(sessionsToActive).date()));
    }

    private void activateSessions(List<ActivateSession> activateSessions) {
        for (ActivateSession activateSession : activateSessions) {
            configurationService.updateSessionWithState(
                    activateSession.id(),
                    SessionState.DRAFT,
                    new ConfigurationService.EditableSessionParams(
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

    private static int findIndexOfConfigurationDay(List<ConfigurationDay> configurationDays, ConfigurationDayId searchingConfigId) {
        for (int index = 0; index < configurationDays.size(); index++) {
            ConfigurationDay configurationDay = configurationDays.get(index);
            if (configurationDay.id().equals(searchingConfigId)) {
                return index;
            }
        }

        throw new NotFoundDayInConfiguration(format("ConfigId %s not found in %s", searchingConfigId, configurationDays));
    }

    private void validateIfTrainingIsFilledProperly(List<ConfigurationDay> configurationDays) {
        List<ConfigurationDay> trainingDaysWithoutExercises = configurationDays.stream()
                .filter(ConfigurationDay::isTrainingDay)
                .filter(trainingDay -> isEmpty(trainingDay.exercises()))
                .toList();

        if (isNotEmpty(trainingDaysWithoutExercises)) {
            log.info("Training is not filled properly. Training days without exercises: {}", trainingDaysWithoutExercises);
            throw new TrainingNotConfiguredProperly(trainingDaysWithoutExercises.stream().map(ConfigurationDay::id).toList());
        }
    }

    record ActivateSession(
            ConfigurationDayId id,
            LocalDate date) {
    }
}
