package com.shapyfy.core.domain.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "exercises")
@Table(name = "exercises")
public class Exercise {

    @EmbeddedId
    private ExerciseId id;

    @Column(name = "translation_key", nullable = false, unique = true)
    private String translationKey;

    protected Exercise() {
        // JPA requires a no-arg constructor
    }

    private Exercise(ExerciseId id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    public ExerciseId getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public static Exercise of(ExerciseId id, String translationKey) {
        return new Exercise(id, translationKey);
    }

    public static Exercise from(String translationKey) {
        return new Exercise(ExerciseId.createNew(), translationKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return id != null && id.equals(exercise.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", translationKey='" + translationKey + '\'' +
                '}';
    }

    @Embeddable
    public static class ExerciseId {
        private UUID id;

        protected ExerciseId() {
            // JPA requires a no-arg constructor
        }

        private ExerciseId(UUID id) {
            this.id = id;
        }

        public UUID getId() {
            return id;
        }

        public static ExerciseId of(UUID id) {
            return new ExerciseId(id);
        }

        public static ExerciseId createNew() {
            return new ExerciseId(UUID.randomUUID());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExerciseId that = (ExerciseId) o;
            return id != null && id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "ExerciseId{" + "id=" + id + '}';
        }
    }
}
