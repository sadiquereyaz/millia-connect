package com.reyaz.core.common.utils.extensions

fun String.toCapSmall(): String {
    return this.lowercase()
        .replaceFirstChar { it.uppercase() }
}