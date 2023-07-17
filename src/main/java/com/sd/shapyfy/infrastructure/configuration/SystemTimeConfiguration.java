package com.sd.shapyfy.infrastructure.configuration;

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
                return LocalDate.now();
            }

            @Override
            public LocalDateTime now() {
                return LocalDateTime.now();
            }
        };
    }
}
