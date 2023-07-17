package com.sd.shapyfy.domain;


import com.sd.shapyfy.domain.model.Training;
import com.sd.shapyfy.domain.model.UserId;

import java.util.List;

public interface TrainingFetcher {

    List<Training> fetchFor(UserId userId);
}
