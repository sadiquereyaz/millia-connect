package com.reyaz.core.ui.helper

import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.runtime.Composable

@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}
