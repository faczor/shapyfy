package com.shapyfy.core.boundary.api.training.adapter;

import com.shapyfy.core.domain.ActivityLogs;
import com.shapyfy.core.domain.PlanDays;
import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanDayApiAdapter {

    private final PlanDays planDays;

    private final ActivityLogs activityLogs;

    public PlanDay getPlanDay(PlanDay.PlanDayId planDayId) {
        PlanDay planDay = planDays.fetchById(planDayId);
        List<ActivityLog> logsForPlan = activityLogs.logsForPlanDay(planDayId);

        return null; //TODO mapping context to Workout Plan
    }


}
