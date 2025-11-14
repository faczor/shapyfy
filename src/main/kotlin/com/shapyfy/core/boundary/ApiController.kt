package com.shapyfy.core.boundary

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api/v1")
abstract class ApiController {
    companion object {
        const val ACCEPT_LANGUAGE_HEADER: String = HttpHeaders.ACCEPT_LANGUAGE
    }
}
