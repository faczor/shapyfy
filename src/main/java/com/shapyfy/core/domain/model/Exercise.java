package com.shapyfy.core.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity(name = "exercises")
@Table(name = "exercises")
@AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
@NoArgsConstructor(access = PROTECTED, force = true)
@Value
public class Exercise {

    @EmbeddedId
    ExerciseId id;

    String name;

    public static Exercise from(String name) {
        return new Exercise(ExerciseId.createNew(), name);
    }

    @Value
    @Embeddable
    @AllArgsConstructor(access = PUBLIC, staticName = "of")
    @NoArgsConstructor(access = PROTECTED, force = true)
    public static class ExerciseId {
        UUID id;

        public static ExerciseId createNew() {
            return new ExerciseId(UUID.randomUUID());
        }
    }
}
