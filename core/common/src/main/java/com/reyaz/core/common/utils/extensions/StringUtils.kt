package com.reyaz.core.common.utils.extensions

object StringUtils {
    fun String.toCapSmall(): String {
        return this.lowercase()
            .replaceFirstChar { it.uppercase() }
    }

    fun String.capitalizeWordLevel(): String {
        return this.split(" ").joinToString(" ") { it.toCapSmall() }
    }
}