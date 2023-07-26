package com.sd.shapyfy.domain.plan;

import com.sd.shapyfy.domain.session.Session;

public record PlanWithActiveSession(
        Plan plan,
        Session session) {
}
