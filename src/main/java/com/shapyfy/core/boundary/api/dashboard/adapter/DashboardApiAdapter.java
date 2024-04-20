package com.shapyfy.core.boundary.api.dashboard.adapter;

import com.shapyfy.core.boundary.api.dashboard.model.DashboardContract;
import com.shapyfy.core.domain.ActivityLogs;
import com.shapyfy.core.domain.TrainingPlanFetcher;
import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;
import com.shapyfy.core.domain.model.TrainingPlan;
import com.shapyfy.core.domain.model.UserId;
import com.shapyfy.core.util.DateRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.shapyfy.core.boundary.api.dashboard.model.DashboardContract.UserPlanContext.PlanStatus.ACTIVE;
import static com.shapyfy.core.boundary.api.dashboard.model.DashboardContract.UserPlanContext.PlanStatus.NOT_CONFIGURED;

@Component
@RequiredArgsConstructor
public class DashboardApiAdapter {

    private final TrainingPlanFetcher trainingPlanFetcher;

    private final ActivityLogs activityLogs;

    private final CalendarMapper calendarMapper;

    public DashboardContract getDashboardForUser(UserId userId, DateRange dateRange) {

        try {
            TrainingPlan trainingPlan = trainingPlanFetcher.fetchForUser(userId);
            List<ActivityLog> logs = activityLogs.fetchFor(dateRange.start(), dateRange.end(),  trainingPlan.getDays().stream().map(PlanDay::getId).toList());

            return new DashboardContract(
                    new DashboardContract.UserPlanContext(
                            trainingPlan.isActive() ? ACTIVE : NOT_CONFIGURED,
                            trainingPlan.getId().getId().toString()
                    ),
                    calendarMapper.map(trainingPlan, logs, dateRange)
            );
            //TODO Create domain exception for missing plan
        } catch (Exception e) {
            return DashboardContract.emptyState(dateRange);
        }
    }
}
