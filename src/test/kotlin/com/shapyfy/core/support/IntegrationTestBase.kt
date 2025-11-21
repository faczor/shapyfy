package com.shapyfy.core.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.shapyfy.core.architecture.security.JwtToken
import com.shapyfy.core.architecture.translation.AiTranslationClient
import com.shapyfy.core.architecture.translation.AiTranslationResult
import com.shapyfy.core.boundary.ApiController
import com.shapyfy.core.domain.Language
import com.shapyfy.core.domain.UserId
import org.mockito.Mockito
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
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
import java.time.Instant
import java.util.UUID

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

    @MockBean
    protected lateinit var aiTranslationClient: AiTranslationClient

    @BeforeEach
    fun resetState() {
        // Delete in correct order due to foreign keys
        jdbcTemplate.update("DELETE FROM workout_sets")
        jdbcTemplate.update("DELETE FROM workout_exercises")
        jdbcTemplate.update("DELETE FROM workouts")
        jdbcTemplate.update("DELETE FROM plan_exercises")
        jdbcTemplate.update("DELETE FROM plan_days")
        jdbcTemplate.update("DELETE FROM workout_plans")
        jdbcTemplate.update("DELETE FROM translations")
        jdbcTemplate.update("DELETE FROM exercises")
        jdbcTemplate.update("DELETE FROM waitlist_signup")
        cacheManager.getCache("translations")?.clear()

        // Setup default AI translation mock behavior
        setupDefaultAiTranslationMock()
    }

    /**
     * Configures the AI translation client mock with a dictionary of known exercise translations.
     * Tests can override this by calling Mockito.when() again for specific inputs.
     */
    private fun setupDefaultAiTranslationMock() {
        val translations = mapOf(
            // Polish exercises
            "przysiady" to AiTranslationResult(Language.PL, "Squat", 0.95),
            "wyciskanie" to AiTranslationResult(Language.PL, "Bench Press", 0.92),
            "martwy ciąg" to AiTranslationResult(Language.PL, "Deadlift", 0.94),
            "podciąganie" to AiTranslationResult(Language.PL, "Pull Up", 0.93),
            // English exercises
            "squat" to AiTranslationResult(Language.EN, "Squat", 0.99),
            "bench press" to AiTranslationResult(Language.EN, "Bench Press", 0.99),
            "deadlift" to AiTranslationResult(Language.EN, "Deadlift", 0.99),
            "pull up" to AiTranslationResult(Language.EN, "Pull Up", 0.99)
        )

        Mockito.`when`(aiTranslationClient.detectAndTranslate(Mockito.anyString())).thenAnswer { invocation ->
            val input = invocation.getArgument<String>(0).lowercase().trim()
            translations[input] ?: AiTranslationResult(
                detectedLanguage = Language.EN,
                englishName = invocation.getArgument<String>(0).replaceFirstChar { it.titlecase() },
                confidence = 0.7
            )
        }
    }

    /**
     * Helper to configure custom AI translation response for a specific input.
     */
    protected fun mockAiTranslation(input: String, result: AiTranslationResult) {
        Mockito.`when`(aiTranslationClient.detectAndTranslate(input)).thenReturn(result)
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

    protected fun getJson(
        path: String,
        acceptLanguage: String? = "pl"
    ): ResultActions {
        val requestBuilder = get(path)
            .accept(MediaType.APPLICATION_JSON)

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
        @JvmStatic
        @DynamicPropertySource
        fun postgresProperties(registry: DynamicPropertyRegistry) {
            val container = TestPostgresContainer.instance
            registry.add("spring.datasource.url") { container.jdbcUrl }
            registry.add("spring.datasource.username") { container.username }
            registry.add("spring.datasource.password") { container.password }
            registry.add("spring.datasource.driver-class-name") { container.driverClassName }
        }
    }
}
