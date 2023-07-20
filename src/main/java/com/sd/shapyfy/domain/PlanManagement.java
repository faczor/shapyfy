package com.sd.shapyfy.domain;

import com.google.common.collect.Iterables;
import com.sd.shapyfy.domain.model.*;
import com.sd.shapyfy.domain.model.exception.TrainingNotFilledProperlyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanManagement implements PlanManagementAdapter {

    private final TrainingPort trainingPort;

    private final TrainingDaysPort trainingDaysPort;

    private final SessionsCreator sessionsCreator;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Training create(TrainingInitialConfiguration initialConfiguration, UserId userId) {
        log.info("Attempt to create training for {}, with {}", userId, initialConfiguration);
        Training initializedTraining = Training.initialize(userId, initialConfiguration);
        Training savedTraining = trainingPort.create(initializedTraining);
        log.info("Created training {}", savedTraining);
        return savedTraining;
    }

    @Override
    public TrainingDay exercisesSelection(TrainingDayId trainingDayId, List<SelectedExercise> selectedExercises, UserId userId) {
        log.info("Attempt to select exercises {} {} with exercises {}", userId, trainingDayId, selectedExercises);
        Training training = trainingPort.fetchFor(trainingDayId);
        validateIfTrainingIsOwnedByUser(userId, trainingDayId, training);
        return trainingDaysPort.selectExercises(trainingDayId, selectedExercises);
    }

    @Override
    public void activate(PlanId planId, TrainingDayId trainingDayId, LocalDate startDate) {
        log.info("Attempt to activate training {} with day {} and start date {}", planId, trainingDayId, startDate);
        Training training = trainingPort.fetchFor(planId);
        validateIfTrainingIsFilledProperly(training.getTrainingDays());

        List<TrainingPort.ActivateSession> sessionForActivation = sessionsCreator.createForActivation(training, trainingDayId, startDate);
        trainingPort.updateTrainingSessions(sessionForActivation);

        applicationEventPublisher.publishEvent(new StartedTrainingEvent(this, training, Iterables.getLast(sessionForActivation).date()));
    }

    private void validateIfTrainingIsFilledProperly(List<TrainingDay> trainingDays) {
        List<TrainingDay> trainingDaysWithoutExercises = trainingDays.stream()
                .filter(TrainingDay::isTrainingDay)
                .filter(trainingDay -> isEmpty(trainingDay.draftSession().getSessionExercises()))
                .toList();

        if (isNotEmpty(trainingDaysWithoutExercises)) {
            log.info("Training is not filled properly. Training days without exercises: {}", trainingDaysWithoutExercises);
            throw new TrainingNotFilledProperlyException(trainingDaysWithoutExercises.stream().map(TrainingDay::getId).toList());
        }
    }

    private static void validateIfTrainingIsOwnedByUser(UserId userId, TrainingDayId trainingDayId, Training training) {
        boolean isUserOwningTraining = training.getUserId().equals(userId);
        if (FALSE.equals(isUserOwningTraining)) {
            throw new UserNotOwningResourceException(String.format("%s is not owning training %s with day %s", userId, training.getId(), trainingDayId));
        }
    }
}
