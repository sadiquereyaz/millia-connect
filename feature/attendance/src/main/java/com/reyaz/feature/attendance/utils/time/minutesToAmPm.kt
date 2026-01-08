package com.reyaz.feature.attendance.utils.time

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun minutesToAmPmString(
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

fun getAmPmString(hour: Float): String {
    return if (hour > 12) "pm" else "am"
}