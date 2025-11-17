package com.shapyfy.core.architecture.waitlist

import com.shapyfy.core.architecture.persistence.WaitlistSignupCrudRepository
import com.shapyfy.core.architecture.persistence.WaitlistSignupEntity
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class WaitlistService(
    private val repository: WaitlistSignupCrudRepository
) {
    fun signup(email: String, platform: String?): WaitlistSignupEntity {
        // Validate email
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(isValidEmail(email)) { "Invalid email format" }

        val normalizedEmail = email.trim().lowercase()

        // Check if already exists
        if (repository.existsByEmail(normalizedEmail)) {
            throw WaitlistEmailAlreadyExistsException(normalizedEmail)
        }

        val entity = WaitlistSignupEntity(
            id = null,
            email = normalizedEmail,
            platform = platform?.trim()?.takeIf { it.isNotBlank() },
            createdAt = Instant.now()
        )

        return try {
            repository.save(entity)
        } catch (e: DataIntegrityViolationException) {
            // Race condition: another request saved the same email
            throw WaitlistEmailAlreadyExistsException(normalizedEmail)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }
}

class WaitlistEmailAlreadyExistsException(val email: String) : RuntimeException("Email already registered: $email")
