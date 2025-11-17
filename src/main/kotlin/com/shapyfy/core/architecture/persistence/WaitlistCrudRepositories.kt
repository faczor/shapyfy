package com.shapyfy.core.architecture.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WaitlistSignupCrudRepository : CrudRepository<WaitlistSignupEntity, Long> {
    fun existsByEmail(email: String): Boolean
}
