package com.shapyfy.core.domain.model

import java.util.Locale

enum class Language(val code: String) {
    EN("en"),
    PL("pl");

    companion object {
        fun from(raw: String?): Language {
            if (raw.isNullOrBlank()) {
                return EN
            }

            val normalized = raw
                .lowercase(Locale.ROOT)
                .substringBefore('-')
                .substringBefore(',')
            return entries.firstOrNull { it.code == normalized } ?: EN
        }
    }
}
