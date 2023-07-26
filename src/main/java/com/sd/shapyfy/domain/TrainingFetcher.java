package com.sd.shapyfy.domain;


import com.sd.shapyfy.domain.model.Plan;
import com.sd.shapyfy.domain.model.UserId;

import java.util.List;

public interface TrainingFetcher {

    List<Plan> fetchFor(UserId userId);
}
