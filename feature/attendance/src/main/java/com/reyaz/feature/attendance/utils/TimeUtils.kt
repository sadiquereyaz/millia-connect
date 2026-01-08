package com.reyaz.feature.attendance.utils

import com.reyaz.core.common.utils.extensions.StringUtils.toCapSmall
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeUtils {
    val currentLocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val currentHourOfDay = currentLocalDateTime.hour
    fun getHourInMinutesFromMidNight(): Int {
        return currentHourOfDay * 60
    }

    val currentMinuteOfDay = currentHourOfDay * 60 + currentLocalDateTime.minute

    fun getAllDaysOfWeak() = DayOfWeek.entries

    fun getDayName(day: DayOfWeek?): String {
        return day?.name?.toCapSmall() ?: "null"
    }

    fun minutesToTimeString(minutes: Int): String {
        val hour = minutes / 60
        val minute = minutes % 60
        val period = if (hour < 12) "am" else "pm"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return String.format("%d:%02d %s", displayHour, minute, period)
    }

    fun getCurrentDayOfWeek(): DayOfWeek {
        return Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .dayOfWeek
    }

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

    fun formatMinutesToTime(minutes: Int): String {
        val hour = minutes / 60
        val minute = minutes % 60
        val period = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return String.format("%d:%02d %s", displayHour, minute, period)
    }
}