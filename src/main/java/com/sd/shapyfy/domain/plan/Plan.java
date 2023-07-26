package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.model.UserId;

public record Plan(
        PlanId id,
        String name,
        UserId userId) {
}
