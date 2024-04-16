package com.shapyfy.core.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    @Id
    private UUID id;
    private String name;
    private MuscleGroup muscleGroup;

    public ExerciseId getId() {
        return new ExerciseId(id);
    }

    public static record ExerciseId(UUID value) {
    }
}
