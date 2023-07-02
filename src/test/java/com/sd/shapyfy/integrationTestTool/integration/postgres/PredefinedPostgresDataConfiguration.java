package com.sd.shapyfy.integrationTestTool.integration.postgres;

import com.sd.shapyfy.integrationTestTool.TestComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.function.Supplier;

@Slf4j
@org.springframework.boot.test.context.TestComponent
public class PredefinedPostgresDataConfiguration implements TestComponent {

    private static final String PROPERTIES_PREFIX = "integration-test.postgres";

    private static final String DYNAMIC_PROPERTY_URL = PROPERTIES_PREFIX + ".url";

    private static final String DYNAMIC_PROPERTY_USERNAME = PROPERTIES_PREFIX + ".username";

    private static final String DYNAMIC_PROPERTY_PASSWORD = PROPERTIES_PREFIX + ".password";

    static final GenericContainer container = new PostgreSQLContainer("postgres:15.3")
            .withDatabaseName("shapyfy")
            .withCommand();

    @Override
    public void setUp() {
        log.debug("Set up postgres test database");
    }

    @Override
    public void cleanUp() {
        log.debug("Cleaning up postgres test database");
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            container.start();
            String hostName = container.getHost() + ":" + container.getFirstMappedPort();
            String connectionUrl = String.format("jdbc:postgresql://%s/postgres?currentSchema=shapyfy", hostName);

            log.info("Created test docker {}", connectionUrl);
            TestPropertyValues.of(
                    DYNAMIC_PROPERTY_URL + "=" + connectionUrl,
                    DYNAMIC_PROPERTY_USERNAME + "=" + "test",
                    DYNAMIC_PROPERTY_PASSWORD + "=" + "test"
            ).applyTo(applicationContext.getEnvironment());
        }

    }
}
