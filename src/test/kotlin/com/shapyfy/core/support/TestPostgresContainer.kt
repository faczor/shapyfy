package com.shapyfy.core.support

import org.testcontainers.containers.PostgreSQLContainer

/**
 * Singleton Testcontainers PostgreSQL instance.
 * This ensures a single container is reused across all test classes,
 * preventing connection pool exhaustion and timing issues.
 *
 * The container is started immediately when accessed and shared across all tests.
 */
object TestPostgresContainer {

    val instance: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:16-alpine")
        .withDatabaseName("core_test")
        .withUsername("core")
        .withPassword("core")
        .also {
            it.start()
        }
}
