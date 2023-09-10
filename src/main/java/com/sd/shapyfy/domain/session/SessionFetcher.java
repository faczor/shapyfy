package com.sd.shapyfy.domain.session;

import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;

public interface SessionFetcher {

    Session fetchBy(SessionId id);
}
