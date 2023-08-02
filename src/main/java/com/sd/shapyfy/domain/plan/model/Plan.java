package com.sd.shapyfy.domain.plan.model;

import com.sd.shapyfy.domain.user.model.UserId;

public record Plan(
        PlanId id,
        String name,
        UserId userId) {
}
