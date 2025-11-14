package com.shapyfy.core.domain.model

import java.text.Normalizer
import java.util.Locale

object NameNormalizer {
    private val unsafeCharacters = Regex("[^a-z0-9]+")

    fun normalize(value: String): String {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) {
            return ""
        }

        val lowerCased = Normalizer
            .normalize(trimmed, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase(Locale.ROOT)

        return unsafeCharacters
            .replace(lowerCased, "_")
            .trim('_')
            .replace(Regex("_+"), "_")
    }
}
