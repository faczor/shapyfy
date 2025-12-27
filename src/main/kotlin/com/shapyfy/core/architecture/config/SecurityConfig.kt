package com.shapyfy.core.architecture.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jwt.JwtClaimValidator
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        incomingRequestLoggingFilter: IncomingRequestLoggingFilter,
        customAuthenticationConverter: CustomAuthenticationConverter
    ): SecurityFilterChain = http
        .csrf { it.disable() }
        .cors { }
        .authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .requestMatchers("/api/v1/public/**").permitAll()
                .requestMatchers("/api/v1/exercises/**").permitAll()
                .requestMatchers("/api/v1/workouts/**").authenticated()
                .anyRequest().permitAll()
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .httpBasic { it.disable() }
        .formLogin { it.disable() }
        .oauth2ResourceServer { oauth2 ->
            oauth2.jwt { jwt ->
                jwt.jwtAuthenticationConverter(customAuthenticationConverter)
            }
        }
        .addFilterBefore(incomingRequestLoggingFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()

    @Bean
    fun jwtDecoder(
        @Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") jwkSetUri: String,
        @Value("\${firebase.project-id}") projectId: String
    ): JwtDecoder {
        val decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()

        // Firebase-specific JWT validation: issuer and audience must match project ID
        val issuerValidator = JwtClaimValidator<String>("iss") { iss ->
            iss == "https://securetoken.google.com/$projectId"
        }
        val audienceValidator = JwtClaimValidator<List<String>>("aud") { aud ->
            aud.contains(projectId)
        }

        decoder.setJwtValidator(
            DelegatingOAuth2TokenValidator(
                JwtValidators.createDefault(),
                issuerValidator,
                audienceValidator
            )
        )

        return decoder
    }
}
