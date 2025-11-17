package com.shapyfy.core.boundary.waitlist

import com.shapyfy.core.architecture.waitlist.WaitlistService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/public/waitlist")
class WaitlistController(
    private val waitlistService: WaitlistService
) {

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: WaitlistSignupRequest): ResponseEntity<Void> {
        waitlistService.signup(
            email = request.email,
            platform = request.platform
        )

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
