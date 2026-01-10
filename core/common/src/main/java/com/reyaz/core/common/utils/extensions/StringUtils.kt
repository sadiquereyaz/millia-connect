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
}