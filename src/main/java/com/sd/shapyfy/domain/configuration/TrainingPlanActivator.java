package com.sd.shapyfy.domain.configuration;

import com.google.common.collect.Iterables;
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

//TODO Validate if training is properly configured check comment
@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingPlanActivator {

    private final TrainingLookup trainingLookup;

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

    //    private void validateIfTrainingIsFilledProperly(List<TrainingDay> trainingDays) {
    //        List<TrainingDay> trainingDaysWithoutExercises = trainingDays.stream()
    //                .filter(TrainingDay::isTrainingDay)
    //                .filter(trainingDay -> isEmpty(trainingDay.draftSession().sessionExercises()))
    //                .toList();
    //
    //        if (isNotEmpty(trainingDaysWithoutExercises)) {
    //            log.info("Training is not filled properly. Training days without exercises: {}", trainingDaysWithoutExercises);
    //            throw new TrainingNotFilledProperlyException(trainingDaysWithoutExercises.stream().map(TrainingDay::getId).toList());
    //        }
    //    }
    //
    //    private static void validateIfTrainingIsOwnedByUser(UserId userId, TrainingDayId trainingDayId, Plan plan) {
    //        boolean isUserOwningTraining = plan.getUserId().equals(userId);
    //        if (FALSE.equals(isUserOwningTraining)) {
    //            throw new UserNotOwningResourceException(String.format("%s is not owning training %s with day %s", userId, plan.getId(), trainingDayId));
    //        }
    //    }
}
