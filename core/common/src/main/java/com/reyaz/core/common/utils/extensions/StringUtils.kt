package com.reyaz.core.common.utils.extensions

import timber.log.Timber
import java.util.Locale

object StringUtils {

    fun String.toCapSmall(locale: Locale = Locale.getDefault()): String {
        return try {
            if (isEmpty()) return this

            val lower = lowercase(locale)
            lower.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(locale) else it.toString()
            }
        } catch (e: Exception) {
            Timber.e(e)
            ""
        }
    }

    fun String.capitalizeWordLevel(locale: Locale = Locale.getDefault()): String {
        return try {
            if (isBlank()) return this

            trim().split(Regex("\\s+"))
                .joinToString(" ") {
                    it.toCapSmall(locale)
                }
        } catch (e: Exception) {
            Timber.e(e)
            ""
        }
    }

    fun String.getShortForm(maxLength: Int = Int.MAX_VALUE): String {
        val trimmed = trim()
        if (trimmed.isEmpty()) return this

        val parts = trimmed.split(Regex("\\s+"))

        return if (parts.size == 1) {
            // single word → first 4 characters
            parts[0]
                .take(maxLength)
                .uppercase(Locale.getDefault())
        } else {
            // multiple words → initials
            parts.joinToString("") {
                it.first().uppercase(Locale.getDefault())
            }.take(maxLength)
        }
    }
}