package com.sd.shapyfy.domain.configuration;

import com.google.common.collect.Iterables;
import com.sd.shapyfy.domain.configuration.SessionService.CreateSessionRequestParams.CreateSessionPartRequestParams;
import com.sd.shapyfy.domain.configuration.SessionService.CreateSessionRequestParams.CreateSessionPartRequestParams.CreateAttributeRequestParams;
import com.sd.shapyfy.domain.configuration.SessionService.CreateSessionRequestParams.CreateSessionPartRequestParams.CreateTrainingExerciseRequestParams;
import com.sd.shapyfy.domain.configuration.event.OnTrainingActivationEvent;
import com.sd.shapyfy.domain.configuration.model.ConfigurationDay;
import com.sd.shapyfy.domain.configuration.model.ConfigurationId;
import com.sd.shapyfy.domain.configuration.model.TrainingConfiguration;
import com.sd.shapyfy.domain.plan.model.ConfigurationDayId;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartState;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionCreator {

    private final SessionService sessionService;

    @EventListener(OnTrainingActivationEvent.class)
    public void createFollowUpsOnTrainingActivation(OnTrainingActivationEvent event) {
        Session session = createSession(event.getTrainingConfiguration(), SessionState.ACTIVE, event.getStartDayId(), event.getTrainingStartDate());

        createSession(event.getTrainingConfiguration(), SessionState.FOLLOW_UP, event.getTrainingConfiguration().configurationDays().get(0).id(), session.lastDate().plusDays(1));
    }

    private Session createSession(TrainingConfiguration trainingConfiguration, SessionState sessionState, ConfigurationDayId startDayId, LocalDate trainingStartDate) {
        log.info("Create session for configuration {} with state {} and startWith {} on {}", trainingConfiguration, sessionState, startDayId, trainingStartDate);
        int indexOfStartDay = Iterables.indexOf(trainingConfiguration.configurationDays(), day -> Objects.equals(day.id(), startDayId));
        SessionService.CreateSessionRequestParams createSessionRequestParams = buildCreateSessionParams(trainingConfiguration, sessionState, trainingStartDate, indexOfStartDay);
        return sessionService.createSession(createSessionRequestParams);
    }

    private SessionService.CreateSessionRequestParams buildCreateSessionParams(TrainingConfiguration trainingConfiguration, SessionState sessionState, LocalDate trainingStartDate, int indexOfStartDay) {
        return new SessionService.CreateSessionRequestParams(
                ConfigurationId.of(trainingConfiguration.sessionId().getValue()),
                sessionState,
                IntStream.range(0, trainingConfiguration.daysPlanAmount()).mapToObj(index -> {
                    ConfigurationDay configurationDay = trainingConfiguration.configurationDays().get(index);

                    return new CreateSessionPartRequestParams(
                            configurationDay.name(),
                            trainingStartDate.plusDays(index),
                            configurationDay.type(),
                            index < indexOfStartDay ? SessionPartState.SKIP : SessionPartState.ACTIVE,
                            configurationDay.exercises().stream().map(exercise -> new CreateTrainingExerciseRequestParams(
                                    exercise.exercise().id(),
                                    exercise.sets(),
                                    exercise.reps(),
                                    exercise.breakBetweenSets(),
                                    exercise.weight())).toList(),
                            trainingConfiguration.configurationAttributes().stream().map(attribute -> new CreateAttributeRequestParams(attribute.id())).toList());
                }).toList()
        );
    }
}
