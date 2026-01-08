package com.reyaz.core.ui.helper

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

inline fun Modifier.debounceClickable(
    enabled: Boolean = true,
    debounceInterval: Long = 1000L,
    crossinline onClick: () -> Unit,
): Modifier = composed {
    var lastClickTime by rememberSaveable { mutableStateOf(0L) }
    clickable(enabled = enabled) {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) < debounceInterval) return@clickable
        lastClickTime = currentTime
        onClick()
    }
}