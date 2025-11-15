package com.shapyfy.core.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.shapyfy.core.architecture.security.JwtToken
import com.shapyfy.core.boundary.ApiController
import com.shapyfy.core.domain.UserId
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Instant
import java.util.UUID

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
abstract class IntegrationTestBase {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    protected lateinit var cacheManager: CacheManager

    @BeforeEach
    fun resetState() {
        // Delete in correct order due to foreign keys
        jdbcTemplate.update("DELETE FROM workout_sets")
        jdbcTemplate.update("DELETE FROM workout_exercises")
        jdbcTemplate.update("DELETE FROM workouts")
        jdbcTemplate.update("DELETE FROM translations")
        jdbcTemplate.update("DELETE FROM exercises")
        cacheManager.getCache("translations")?.clear()
    }

    protected fun postJson(
        path: String,
        body: Any,
        acceptLanguage: String? = "pl"
    ): ResultActions {
        val requestBuilder = post(path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body))

        if (acceptLanguage != null) {
            requestBuilder.header(ApiController.ACCEPT_LANGUAGE_HEADER, acceptLanguage)
        }

        return mockMvc.perform(requestBuilder)
    }

    protected fun postJsonAuthenticated(
        path: String,
        body: Any,
        userId: UUID,
        acceptLanguage: String? = "pl"
    ): ResultActions {
        val requestBuilder = post(path)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body))
            .principal(createTestJwtToken(userId))

        if (acceptLanguage != null) {
            requestBuilder.header(ApiController.ACCEPT_LANGUAGE_HEADER, acceptLanguage)
        }

        return mockMvc.perform(requestBuilder)
    }

    protected fun getJsonAuthenticated(
        path: String,
        userId: UUID,
        acceptLanguage: String? = "pl"
    ): ResultActions {
        val requestBuilder = get(path)
            .accept(MediaType.APPLICATION_JSON)
            .principal(createTestJwtToken(userId))

        if (acceptLanguage != null) {
            requestBuilder.header(ApiController.ACCEPT_LANGUAGE_HEADER, acceptLanguage)
        }

        return mockMvc.perform(requestBuilder)
    }

    /**
     * Creates a test JWT token for integration tests
     */
    private fun createTestJwtToken(userId: UUID): JwtToken {
        val jwt = Jwt.withTokenValue("test-token")
            .header("alg", "none")
            .claim("sub", userId.toString())
            .claim("user_id", userId.toString())
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build()

        return JwtToken(jwt, UserId.from(userId.toString()))
    }

    companion object {
        @Container
        @JvmStatic
        val postgres: TestPostgresContainer = TestPostgresContainer("postgres:16-alpine").apply {
            withDatabaseName("core_test")
            withUsername("core")
            withPassword("core")
        }

        @JvmStatic
        @DynamicPropertySource
        fun postgresProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
            registry.add("spring.datasource.driver-class-name") { postgres.driverClassName }
        }
    }
}
