package com.shapyfy.core.architecture.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("waitlist_signup")
data class WaitlistSignupEntity(
    @Id
    val id: Long?,

    val email: String,

    val platform: String?,

    @Column("created_at")
    val createdAt: Instant
)
