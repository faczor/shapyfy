package com.shapyfy.core.infrastructure.storage.postgres;

import com.shapyfy.core.domain.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseJpaRepository extends JpaRepository<Exercise, Exercise.ExerciseId>{
}
