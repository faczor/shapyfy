package com.sd.shapyfy.infrastructure.services.postgres.sessions.component;

import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostgresSessionExerciseRepository extends JpaRepository<SessionExerciseEntity, UUID> {

    @Query(value = "select se FROM SessionExerciseEntity se " +
            "WHERE se.exercise.id = :exerciseId " +
            "AND se.isFinished = true " +
            "AND se.sessionPart.session.training.userId = :userId")
    List<SessionExerciseEntity> fetchAllFinished(
            UUID exerciseId, String userId);
}
