package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostgresSessionRepository extends JpaRepository<SessionEntity, UUID> {
}
