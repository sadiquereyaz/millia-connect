package com.reyaz.feature.attendance.domain.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class LectureItem(
    val id: Long,
    val startTimeMinute: Int,       // minute since midnight
    val endTimeMinute: Int,
    val title: String,
    val location: String?,
    val warning: String,
    val percentage: Int,
    val status: AttendanceStatus
) {
    val timeText: String = "${minutesToAmPm(startTimeMinute)}\n—\n${minutesToAmPm(endTimeMinute)}"
}

private fun getAmPmString(hour: Float): String {
    return if (hour > 12) "pm" else "am"
}

fun minutesToAmPm(
    timeInMinutes: Int,
    locale: Locale = Locale.getDefault()
): String {
    val hour = timeInMinutes / 60
    val minute = timeInMinutes % 60

    val calendar = Calendar.getInstance(locale).apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }

    val formatter = SimpleDateFormat("hh:mm a", locale)
    return formatter.format(calendar.time)
}