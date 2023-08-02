package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostgresSessionExerciseRepository extends JpaRepository<SessionExerciseEntity, UUID> {
}
