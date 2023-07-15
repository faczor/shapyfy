package com.sd.shapyfy.infrastructure.services.postgres.sessions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostgresSessionRepository extends JpaRepository<SessionEntity, UUID> {
}
