package com.shapyfy.core.support

import org.testcontainers.containers.PostgreSQLContainer

class TestPostgresContainer(imageName: String) : PostgreSQLContainer<TestPostgresContainer>(imageName)
