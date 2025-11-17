package com.shapyfy.core.boundary.waitlist

import com.shapyfy.core.support.IntegrationTestBase
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class WaitlistControllerIntegrationTest : IntegrationTestBase() {

    @Test
    fun `should successfully add email to waitlist`() {
        // Given
        val request = WaitlistSignupRequest(
            email = "test@example.com",
            platform = "web"
        )

        // When & Then
        postWaitlistSignup(request)
            .andExpect(status().isCreated)
    }

    @Test
    fun `should return conflict when email already exists`() {
        // Given
        val request = WaitlistSignupRequest(
            email = "duplicate@example.com",
            platform = "iOS"
        )

        // When - First signup succeeds
        postWaitlistSignup(request)
            .andExpect(status().isCreated)

        // Then - Second signup with same email fails
        postWaitlistSignup(request)
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value("Email already registered"))
            .andExpect(jsonPath("$.details").value("This email is already on the waitlist"))
    }

    @Test
    fun `should normalize email to lowercase`() {
        // Given
        val request = WaitlistSignupRequest(
            email = "Test@EXAMPLE.COM",
            platform = "Android"
        )

        // When & Then
        postWaitlistSignup(request)
            .andExpect(status().isCreated)
    }

    @Test
    fun `should allow null platform`() {
        // Given
        val request = WaitlistSignupRequest(
            email = "noplatform@example.com",
            platform = null
        )

        // When & Then
        postWaitlistSignup(request)
            .andExpect(status().isCreated)
    }

    @Test
    fun `should fail when email is blank`() {
        // Given
        val request = WaitlistSignupRequest(
            email = "",
            platform = "web"
        )

        // When & Then
        postWaitlistSignup(request)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should fail when email format is invalid`() {
        // Given
        val request = WaitlistSignupRequest(
            email = "not-an-email",
            platform = "web"
        )

        // When & Then
        postWaitlistSignup(request)
            .andExpect(status().isBadRequest)
    }

    private fun postWaitlistSignup(request: WaitlistSignupRequest): ResultActions =
        postJson("/api/v1/public/waitlist/signup", request, acceptLanguage = null)
}
