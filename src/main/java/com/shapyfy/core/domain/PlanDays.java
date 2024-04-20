package com.shapyfy.core.domain;

import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.port.PlanDayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanDays {

    private final PlanDayRepository planDayRepository;

    public PlanDay fetchById(PlanDay.PlanDayId planDayId) {
        log.info("Fetching PlanDay by ID: {}", planDayId);
        return planDayRepository.getById(planDayId);
    }
}
