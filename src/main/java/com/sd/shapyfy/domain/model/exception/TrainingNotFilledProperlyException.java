package com.sd.shapyfy.domain.model.exception;

import com.sd.shapyfy.domain.model.TrainingDayId;
import lombok.Getter;

import java.util.List;

public class TrainingNotFilledProperlyException extends RuntimeException {

    @Getter
    List<TrainingDayId> trainingDayIds;

    public TrainingNotFilledProperlyException(List<TrainingDayId> trainingDayIds) {
        super(String.format("Training is not filled properly. Training days without exercises: %s", trainingDayIds));
        this.trainingDayIds = trainingDayIds;
    }
}
