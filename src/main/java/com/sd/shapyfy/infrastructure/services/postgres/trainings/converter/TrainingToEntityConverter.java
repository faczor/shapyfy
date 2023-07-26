package com.sd.shapyfy.infrastructure.services.postgres.trainings.converter;

import com.sd.shapyfy.domain.model.Plan;
import com.sd.shapyfy.domain.plan.PlanId;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.TrainingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class TrainingToEntityConverter {

    public TrainingEntity convert(Plan plan) {
        return new TrainingEntity(
                Optional.ofNullable(plan.getId()).map(PlanId::getValue).orElse(null),
                plan.getUserId().getValue(),
                plan.getName(),
                new ArrayList<>()
        );
    }
}
