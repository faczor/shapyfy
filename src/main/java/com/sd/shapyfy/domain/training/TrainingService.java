package com.sd.shapyfy.domain.training;

import com.sd.shapyfy.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService implements TrainingAdapter {

    private final TrainingPort trainingPort;

    @Override
    public Training createTraining(UserId userId) {
        log.info("Attempt to create training for [{}]", userId);
        Training initializedTraining = Training.initialize(userId);
        Training savedTraining = trainingPort.save(initializedTraining);
        log.info("Created training {}", savedTraining);
        return savedTraining;
    }
}
