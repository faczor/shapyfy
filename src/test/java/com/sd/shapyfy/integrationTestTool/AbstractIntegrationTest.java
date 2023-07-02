package com.sd.shapyfy.integrationTestTool;

import com.sd.shapyfy.ShapyfyApplication;
import com.sd.shapyfy.integrationTestTool.integration.postgres.PredefinedPostgresDataConfiguration;
import com.sd.shapyfy.integrationTestTool.spring.security.TestUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = ShapyfyApplication.class)
@ContextConfiguration(initializers = PredefinedPostgresDataConfiguration.Initializer.class)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    private int serverPort;

    @Autowired(required = false)
    private List<TestComponent> testComponents;

    protected final void prepareTestComponents() {
        log.debug("SET UP TEST COMPONENTS");
        Iterator<TestComponent> iterator = testComponents.iterator();
        iterator.forEachRemaining(TestComponent::setUp);
    }

    protected final void cleanUpTestComponents() {
        log.debug("CLEAN UP TEST COMPONENTS");
        Iterator<TestComponent> iterator = new LinkedList<>(testComponents).descendingIterator();
        iterator.forEachRemaining(TestComponent::cleanUp);
    }


    protected final ApiRequestAssertion as(TestUser user) {
        return as(Optional.ofNullable(user));
    }

    private ApiRequestAssertion as(Optional<TestUser> user) {
        return configurer -> {
            log.debug("Preparing api request as {}", user);

            RequestSpecification specification = RestAssured.given()
                    //
                    .log().all()
                    .baseUri("http://localhost:" + serverPort);

            RequestSpecification authorizedSpecification = user
                    .map(u -> specification.header("Authorization", "Bearer " + provideTokenFor(u)))
                    .orElse(specification);

            RequestSpecification rq = authorizedSpecification
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON);

            return configurer.apply(rq).then().log().all().assertThat();
        };
    }

    @SneakyThrows
    private String provideTokenFor(TestUser user) {
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        return Jwts.builder()
                .claim("user_id", user.getId())
                .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
                .signWith(
                        SignatureAlgorithm.HS256,
                        new String(base64.encode("f97e7eeecf01c808bf4ab9397340bfb2982e8475".getBytes()))
                ).compact();
    }

    public interface ApiRequestAssertion {
        ValidatableResponse assertRequest(Function<RequestSpecification, Response> requestFunction);
    }
}
