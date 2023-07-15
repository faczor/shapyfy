package com.sd.shapyfy.domain;

import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingPort;
import com.sd.shapyfy.domain.trainingDay.TrainingDayId;
import com.sd.shapyfy.domain.trainingDay.TrainingDaysPort;
import com.sd.shapyfy.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Boolean.FALSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingManagement implements TrainingManagementAdapter {

    private final TrainingPort trainingPort;

    private final TrainingDaysPort trainingDaysPort;

    @Override
    public Training create(TrainingInitialConfiguration initialConfiguration, UserId userId) {
        log.info("Attempt to create training for {}, with {}", userId, initialConfiguration);
        Training initializedTraining = Training.initialize(userId, initialConfiguration);
        Training savedTraining = trainingPort.create(initializedTraining);
        log.info("Created training {}", savedTraining);
        return savedTraining;
    }

    @Override
    public Training.TrainingDay exercisesSelection(TrainingDayId trainingDayId, List<SelectedExercise> selectedExercises, UserId userId) {
        log.info("Attempt to select exercises {} {} with exercises {}", userId, trainingDayId, selectedExercises);
        Training training = trainingPort.fetchFor(trainingDayId);
        validateIfTrainingIsOwnedByUser(userId, trainingDayId, training);
        return trainingDaysPort.selectExercises(trainingDayId, selectedExercises);
    }

    private static void validateIfTrainingIsOwnedByUser(UserId userId, TrainingDayId trainingDayId, Training training) {
        boolean isUserOwningTraining = training.getUserId().equals(userId);
        if (FALSE.equals(isUserOwningTraining)) {
            throw new UserNotOwningResourceException(String.format("%s is not owning training %s with day %s", userId, training.getId(), trainingDayId));
        }
    }
}
