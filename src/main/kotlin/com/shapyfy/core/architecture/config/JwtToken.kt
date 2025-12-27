package com.shapyfy.core.architecture.config

import com.shapyfy.core.domain.UserId
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken

/**
 * Custom JWT authentication token that wraps the Firebase JWT and extracts the UserId.
 * This is used throughout the application to access the authenticated user's ID.
 */
class JwtToken(
    jwt: Jwt,
    val userId: UserId
) : AbstractOAuth2TokenAuthenticationToken<Jwt>(jwt, listOf(SimpleGrantedAuthority("ROLE_USER"))) {

    init {
        isAuthenticated = true
    }

    override fun getTokenAttributes(): Map<String, Any> = token.claims
}
