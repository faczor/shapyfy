package com.shapyfy.core.boundary

import org.springframework.http.HttpStatus
import java.util.UUID

class SyncException(
    val status: HttpStatus,
    val id: UUID?,
    val translationKey: String?,
    val availableTranslations: Map<String, String> = emptyMap(),
    private val errorMessage: String
) : RuntimeException(errorMessage) {

    fun toResponse(): SyncErrorResponse = SyncErrorResponse(
        id = id,
        message = errorMessage,
        translationKey = translationKey,
        availableTranslations = availableTranslations
    )
}

data class SyncErrorResponse(
    val id: UUID?,
    val message: String,
    val translationKey: String?,
    val availableTranslations: Map<String, String>
)
