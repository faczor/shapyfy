package com.sd.shapyfy.domain;


import com.sd.shapyfy.domain.training.Training;
import com.sd.shapyfy.domain.user.UserId;

import java.util.List;

public interface TrainingFetcher {

    List<Training> fetchFor(UserId userId);
}
