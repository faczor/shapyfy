package com.shapyfy.core.boundary.exercises

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

        val normalized = unsafeCharacters
            .replace(lowerCased, "_")
            .trim('_')
            .replace(Regex("_+"), "_")

        // Canonical names must start with a letter (domain validation rule)
        // If it starts with a number, prefix with 'ex_'
        return if (normalized.isNotEmpty() && normalized[0].isDigit()) {
            "ex_$normalized"
        } else {
            normalized
        }
    }
}