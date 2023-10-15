package com.sd.shapyfy.domain.session;

import com.sd.shapyfy.domain.configuration.SessionService;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionUpdater {

    private final SessionService sessionService;

    public void updateStatus(SessionId sessionId, SessionState state) {
        log.info("Attempt to update status {} of {}", state, sessionId);
        sessionService.updateStatus(sessionId, state);
        log.info("Status updated");
    }
}
