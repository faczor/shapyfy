package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostgresSessionPartRepository extends JpaRepository<SessionPartEntity, UUID> {
}
