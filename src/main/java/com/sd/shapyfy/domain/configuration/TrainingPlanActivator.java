package com.sd.shapyfy.domain.configuration;

import com.google.common.collect.Iterables;
import com.sd.shapyfy.domain.configuration.SessionService.EditSessionParams;
import com.sd.shapyfy.domain.configuration.SessionService.EditSessionParams.EditSessionPart;
import com.sd.shapyfy.domain.configuration.event.OnTrainingActivationEvent;
import com.sd.shapyfy.domain.configuration.exception.NotFoundDayInConfiguration;
import com.sd.shapyfy.domain.configuration.exception.TrainingNotConfiguredProperly;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.model.PlanId;
import com.sd.shapyfy.domain.plan.model.SessionPartId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.isEmpty;
import static com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState.ACTIVE;
import static com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState.SKIP;
import static java.lang.String.format;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingPlanActivator {

    private final TrainingLookup trainingLookup;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SessionService sessionService;

    public void activate(PlanId planId, SessionPartId sessionPartId, LocalDate startDate) {
        log.info("Attempt to activate {} by {} on {}", planId, sessionPartId, startDate);
        TrainingConfiguration trainingConfiguration = trainingLookup.configurationFor(planId);
        validateIfTrainingIsFilledProperly(trainingConfiguration.configurationDays());
        //
        int indexOfConfigurationDay = findIndexOfConfigurationDay(trainingConfiguration.configurationDays(), sessionPartId);
        EditSessionParams sessionEditParamsForActivation = prepareSessionForActivation(indexOfConfigurationDay, trainingConfiguration.configurationDays(), startDate);
        sessionService.updateSession(trainingConfiguration.sessionId(), sessionEditParamsForActivation);

        //TODO move event publishing to interface
        applicationEventPublisher.publishEvent(new OnTrainingActivationEvent(this, trainingConfiguration, Iterables.getLast(sessionEditParamsForActivation.editSessionPart()).editSessionPartParams().date()));
    }

    private EditSessionParams prepareSessionForActivation(int indexOfStartingDay, List<ConfigurationDay> configurationDays, LocalDate startDate) {
        int index = 0;
        ArrayList<EditSessionPart> partEditParams = new ArrayList<>();

        for (ConfigurationDay configurationDay : configurationDays) {
            EditSessionPart editSessionPart = new EditSessionPart(
                    configurationDay.id(),
                    new SessionService.EditSessionPartParams(
                            null,
                            null,
                            startDate.plusDays(index),
                            ACTIVE, //TODO
                            null
                    )
            );
            partEditParams.add(editSessionPart);
            index++;
        }


        return new EditSessionParams(
                SessionState.ACTIVE,
                partEditParams
        );
    }

    private static int findIndexOfConfigurationDay(List<ConfigurationDay> configurationDays, SessionPartId sessionPartId) {
        for (int index = 0; index < configurationDays.size(); index++) {
            ConfigurationDay configurationDay = configurationDays.get(index);
            if (configurationDay.id().equals(sessionPartId)) {
                return index;
            }
        }
        throw new NotFoundDayInConfiguration(format("ConfigId %s not found in %s", sessionPartId, configurationDays));
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

}
