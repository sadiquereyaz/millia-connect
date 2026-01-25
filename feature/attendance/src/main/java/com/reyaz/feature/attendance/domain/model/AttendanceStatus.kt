package com.reyaz.feature.attendance.domain.model

import androidx.compose.ui.graphics.Color

/*enum class AttendanceStatus{
    PRESENT,
    ABSENT,
    CANCELLED
}*/

enum class AttendanceStatus(
    val lightColor: Color,
    val darkColor: Color,
    val title: String
) {
    /*NOT_COUNTED(
        lightColor = Color(0xFF414942),   // Grey 700
        darkColor = Color(0xFFC0C9C0),    // Light Grey
        title = "Not Counted"
    ),*/

    NOT_COUNTED(
        lightColor = Color(0xFFF9A825),   // Amber 800
        darkColor = Color(0xFFFFE082),    // Light Amber
        title = "Not Counted"
    ),

    ABSENT(
        lightColor = Color(0xFFC62828),   // Red 800
        darkColor = Color(0xFFEF9A9A),    // Light Red
        title = "Absent"
    ),

    PRESENT(
        lightColor = Color(0xFF2E7D32),   // Green 800
        darkColor = Color(0xFF81C784),    // Light Green
        title = "Present"
    );

    fun getColor(isDark: Boolean): Color {
        return if (isDark) darkColor else lightColor
    }

    fun getDisplayText(isCompact: Boolean): String {
        return if (isCompact) title.first().toString() else title
    }
}