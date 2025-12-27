package com.shapyfy.core.architecture.config

import com.shapyfy.core.domain.UserId
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

/**
 * Converts Firebase JWT tokens into our custom JwtToken authentication objects.
 * Extracts the user ID from the Firebase JWT "sub" claim (which contains the Firebase UID).
 */
@Component
class CustomAuthenticationConverter : Converter<Jwt, AbstractAuthenticationToken> {

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        // Firebase stores the user ID in the "sub" claim as a string (e.g., "CJ0MJjT8YSWHgSKddm7UKhqI62B2")
        val userIdString = jwt.subject
            ?: throw IllegalStateException("JWT subject (user ID) not found")

        val userId = UserId.from(userIdString)

        return JwtToken(jwt, userId)
    }
}
