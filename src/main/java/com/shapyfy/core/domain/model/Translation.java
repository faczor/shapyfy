package com.shapyfy.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity(name = "translations")
@Table(
        name = "translations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_translation_key_language", columnNames = {"translation_key", "language"})
        },
        indexes = {
                @Index(name = "idx_translation_key_lang", columnList = "translation_key, language"),
                @Index(name = "idx_category", columnList = "category")
        }
)
@AllArgsConstructor(access = PUBLIC, staticName = "of")
@NoArgsConstructor(access = PROTECTED, force = true)
@Getter
@EqualsAndHashCode(of = {"translationKey", "language"})
@ToString
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "translation_key", nullable = false)
    private String translationKey;

    @Column(name = "language", nullable = false, length = 2)
    private String language;

    @Column(name = "value", nullable = false, columnDefinition = "TEXT")
    private String value;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public static Translation create(String translationKey, String language, String value, String category) {
        return new Translation(null, translationKey, language, value, category, null, null);
    }
}
