package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.port.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityLogAdapter implements ActivityLogRepository {

    private final ActivityLogJpaRepository activityLogJpaRepository;

    @Override
    public List<ActivityLog> findAll(LocalDate from, LocalDate to, List<PlanDay.PlanDayId> planDayIds) {
        return activityLogJpaRepository.findAllByDateBetweenAndPlanDayIdIn(from, to, planDayIds);
    }

    @Override
    public List<ActivityLog> findAllByPlanDayId(PlanDay.PlanDayId planDayId) {
        return activityLogJpaRepository.findAllByPlanDayId(planDayId);
    }

    @Override
    public ActivityLog save(ActivityLog activityLog) {
        return activityLogJpaRepository.save(activityLog);
    }
}
