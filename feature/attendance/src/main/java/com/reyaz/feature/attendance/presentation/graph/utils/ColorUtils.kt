package com.reyaz.feature.attendance.presentation.graph.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.random.Random
object ColorUtils {
    fun readableColor(isDarkTheme: Boolean): Color {
        val hue = Random.nextFloat() * 360f

        val saturation = 0.7f
        val value = if (isDarkTheme) {
            Random.nextFloat() * 0.2f + 0.8f
        } else {
            Random.nextFloat() * 0.2f + 0.4f
        }

        return Color.hsv(hue, saturation, value)
    }


}

