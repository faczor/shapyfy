package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.PlanDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanDayJpaRepository extends JpaRepository<PlanDay, PlanDay.PlanDayId> {
}
