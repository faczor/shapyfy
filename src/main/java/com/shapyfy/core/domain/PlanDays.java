package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.port.PlanDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanDays {

    private final PlanDayRepository planDayRepository;

    public PlanDay fetchById(PlanDay.PlanDayId planDayId) {
        return planDayRepository.getById(planDayId);
    }
}
