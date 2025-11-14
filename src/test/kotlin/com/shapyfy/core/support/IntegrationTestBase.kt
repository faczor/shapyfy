package com.shapyfy.core.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.shapyfy.core.boundary.ApiController
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

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
