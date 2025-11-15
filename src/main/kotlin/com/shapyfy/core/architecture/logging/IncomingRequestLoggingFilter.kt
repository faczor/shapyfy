package com.shapyfy.core.architecture.logging

import com.nimbusds.jwt.SignedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class IncomingRequestLoggingFilter : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestId = UUID.randomUUID().toString()
        val pathWithQuery = pathWithQuery(request)
        val userId = extractUserIdFromJwt(request)

        if (userId != null) {
            log.info("Incoming request [{}]: {} {} (user: {})", requestId, request.method, pathWithQuery, userId)
        } else {
            log.info("Incoming request [{}]: {} {}", requestId, request.method, pathWithQuery)
        }

        try {
            filterChain.doFilter(request, response)
        } finally {
            if (userId != null) {
                log.info(
                    "Outgoing response [{}]: {} {} -> {} (user: {})",
                    requestId,
                    request.method,
                    pathWithQuery,
                    response.status,
                    userId
                )
            } else {
                log.info(
                    "Outgoing response [{}]: {} {} -> {}",
                    requestId,
                    request.method,
                    pathWithQuery,
                    response.status
                )
            }
        }
    }

    /**
     * Extracts user ID from JWT token in Authorization header.
     * Returns null if header is missing, invalid, or parsing fails.
     */
    private fun extractUserIdFromJwt(request: HttpServletRequest): String? {
        return try {
            val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null

            if (!authHeader.startsWith(BEARER_PREFIX)) {
                return null
            }

            val jwtToken = authHeader.removePrefix(BEARER_PREFIX).trim()
            val signedJwt = SignedJWT.parse(jwtToken)

            // Firebase stores user ID in "sub" claim
            signedJwt.jwtClaimsSet.subject
        } catch (e: Exception) {
            // Log parsing errors at debug level to avoid noise
            log.debug("Failed to extract user ID from JWT: {}", e.message)
            null
        }
    }

    private fun pathWithQuery(request: HttpServletRequest): String {
        val query = request.queryString?.takeIf { it.isNotBlank() }?.let { "?$it" } ?: ""
        return "${request.requestURI}$query"
    }
}
