package com.reyaz.feature.attendance.presentation.graph.utils

import androidx.compose.ui.graphics.Color
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

    val color = listOf(
        // pair(light color , dark color)
        Pair(Color(0xFFe74f68), Color(0xFF7f1c39)),
        Pair(Color(0xFF67bc26), Color(0xFF2d4d1a)),
        Pair(Color(0xFFe2cc13), Color(0xFF6c4b17)),
        Pair(Color(0xFF2f61ff), Color(0xFF102a9f)),
        Pair(Color(0xFFbcce14), Color(0xFF4a5413)),
        Pair(Color(0xFFee2aff), Color(0xFF7e0b84)),
        Pair(Color(0xFF967c5c), Color(0xFF4e3c35)),
        Pair(Color(0xFF00e21e), Color(0xFF0c5b1b)),
        Pair(Color(0xFF66a6ca), Color(0xFF244256)),
        Pair(Color(0xFF6c8c5b), Color(0xFF303d2a)),
        Pair(Color(0xFFc17293), Color(0xFF673450)),
        Pair(Color(0xFF2c9792), Color(0xFF1c4545)),
    )

    fun getColor(isDarkTheme: Boolean, index: Int? = null): Color {
        val safeIndex = (index ?: Random.nextInt(color.size)) % color.size
        val (darkModeColor, lightModeColor) = color[safeIndex]
        return if (isDarkTheme) darkModeColor else lightModeColor
    }
}

