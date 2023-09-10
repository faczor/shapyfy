package com.sd.shapyfy.integrationTestTool.spring.configuration;

import com.sd.shapyfy.SystemTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class SystemTimeConfiguration {

    @Bean
    public SystemTime systemTime() {
        return new SystemTime() {
            @Override
            public LocalDate today() {
                return LocalDate.of(2023, 1, 1);
            }

            @Override
            public LocalDateTime now() {
                return LocalDateTime.of(2023, 1, 1, 12, 0, 0);
            }
        };
    }
}
