package com.shapyfy.core.architecture.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table("exercises")
class ExerciseEntity(
    @Id
    @Column("id")
    private val _id: UUID,

    @Column("translation_key")
    val name: String,

    @Column("created_at")
    val createdAt: Instant,

    @Column("updated_at")
    val updatedAt: Instant?
) : Persistable<UUID> {

    @Transient
    private var isNewEntity: Boolean = false

    override fun getId(): UUID = _id
    override fun isNew(): Boolean = isNewEntity

    companion object {
        fun new(
            id: UUID,
            name: String,
            createdAt: Instant,
            updatedAt: Instant?
        ): ExerciseEntity = ExerciseEntity(
            _id = id,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt
        ).apply {
            isNewEntity = true
        }
    }
}

@Table("translations")
data class TranslationEntity(
    @Id
    val id: Long?,

    @Column("translation_key")
    val translationKey: String,

    val language: String,

    val value: String,

    val category: String,

    @Column("normalized_value")
    val normalizedValue: String,

    @Column("created_at")
    val createdAt: Instant?,

    @Column("updated_at")
    val updatedAt: Instant?
)
