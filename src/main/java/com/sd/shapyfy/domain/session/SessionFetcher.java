package com.sd.shapyfy.domain.session;

import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.user.model.UserId;

import java.util.List;

public interface SessionFetcher {

    Session fetchBy(SessionId id);

    List<Session> fetchAllBy(UserId userId);
}
