package com.sd.shapyfy.domain.trainingDay;

import com.sd.shapyfy.domain.UserNotOwningResourceException;
import com.sd.shapyfy.domain.exercises.ExerciseId;
import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.training.TrainingPort;
import com.sd.shapyfy.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Boolean.FALSE;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingDaysService implements TrainingDaysAdapter {

    private final TrainingPort trainingPort;

    private final TrainingDaysPort trainingDaysPort;

    @Override
    public Training.TrainingDay selectExercises(UserId userId, TrainingDayId trainingDayId, List<ExerciseDetails> selectedExercises) {
        log.info("Attempt to select exercises {} {} with exercises {}", userId, trainingDayId, selectedExercises);
        Training training = trainingPort.fetchFor(trainingDayId);
        isUserOwningTraining(userId, trainingDayId, training);
        return trainingDaysPort.selectExercises(trainingDayId, selectedExercises);
    }

    private static void isUserOwningTraining(UserId userId, TrainingDayId trainingDayId, Training training) {
        boolean isUserOwningTraining = training.getUserId().equals(userId);
        if (FALSE.equals(isUserOwningTraining)) {
            throw new UserNotOwningResourceException(String.format("%s is not owning training %s with day %s", userId, training.getId(), trainingDayId));
        }
    }
}
