package com.sd.shapyfy.integrationTestTool.spring.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.time.Instant;

@TestConfiguration
public class SecurityTestConfiguration {

    @Bean
    public TestTokenProvider testTokenProvider() {
        return new TestTokenService();
    }

    @RequiredArgsConstructor
    private static class TestTokenService implements TestTokenProvider {

        @Override
        public String provideFor(TestUser user) {

            return Jwts.builder()
                    .claim("user_id", user.getId())
                    .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                    .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
                    .signWith(
                            SignatureAlgorithm.HS256,
                            TextCodec.BASE64.decode("f97e7eeecf01c808bf4ab9397340bfb2982e8475")
                    ).compact();
        }
    }
}
