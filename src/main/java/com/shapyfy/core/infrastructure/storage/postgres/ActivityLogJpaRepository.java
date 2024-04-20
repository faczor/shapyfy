package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.ActivityLog;
import com.shapyfy.core.domain.model.PlanDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityLogJpaRepository extends JpaRepository<ActivityLog, ActivityLog.ActivityLogId> {

    List<ActivityLog> findAllByPlanDayId(PlanDay.PlanDayId planDayId);


    List<ActivityLog> findAllByDateBetweenAndPlanDayIdIn(LocalDate from, LocalDate to, List<PlanDay.PlanDayId> planDayIds);
}
