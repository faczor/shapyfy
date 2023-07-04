package com.sd.shapyfy.domain.training;

import com.sd.shapyfy.domain.trainingDay.TrainingDayId;

public interface TrainingPort {

    Training save(Training training);

    Training fetchFor(TrainingDayId trainingDayId);
}
