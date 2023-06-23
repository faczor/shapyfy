package com.sd.shapyfy.domain.training;

import com.sd.shapyfy.domain.user.UserId;

public interface TrainingAdapter {

    Training createTraining(UserId userId);
}
