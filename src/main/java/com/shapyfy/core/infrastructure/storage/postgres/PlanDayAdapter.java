package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.port.PlanDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanDayAdapter implements PlanDayRepository {

    private final PlanDayJpaRepository planDayJpaRepository;

    @Override
    public PlanDay getById(PlanDay.PlanDayId planDayId) {
        return planDayJpaRepository.findById(planDayId)
                .orElseThrow(() -> new RuntimeException("Plan day not found for id " + planDayId.getId()));
    }
}
