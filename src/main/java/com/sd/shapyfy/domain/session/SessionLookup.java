package com.sd.shapyfy.domain.session;

import com.sd.shapyfy.domain.exercise.SessionPartId;
import com.sd.shapyfy.domain.plan.model.Session;
import com.sd.shapyfy.domain.plan.model.SessionId;
import com.sd.shapyfy.domain.plan.model.SessionPart;
import com.sd.shapyfy.domain.user.model.UserId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionLookup {

    private final SessionFetcher sessionFetcher;

    public SessionPart lookupPart(SessionId sessionId, SessionPartId partId) {
        log.info("Lookup part {} {}", sessionId, partId);
        Session session = sessionFetcher.fetchBy(sessionId);
        SessionPart sessionPart = session.sessionParts().stream().filter(part -> Objects.equals(part.id(), partId)).findFirst().orElseThrow();//TODO proper exception

        return sessionPart;
    }

    public Session lookup(SessionId sessionId) {
        log.info("Lookup session {}", sessionId);
        return sessionFetcher.fetchBy(sessionId);
    }

    public List<Session> lookupFollowUp(UserId userId) {
        return sessionFetcher.fetchAllBy(userId)
                .stream().filter(session -> session.state() == SessionState.FOLLOW_UP)
                .toList();
    }
}
