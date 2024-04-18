package com.shapyfy.core.domain.port;

import com.shapyfy.core.domain.model.PlanDay;

public interface PlanDayRepository {
    PlanDay getById(PlanDay.PlanDayId planDayId);
}
