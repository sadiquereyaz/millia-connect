package com.reyaz.feature.attendance.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import timber.log.Timber
import java.util.Locale

object TimeUtils {

    /* -------------------- Core providers -------------------- */

    private fun now(): LocalDateTime =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    fun today(): LocalDate =
        Clock.System.todayIn(TimeZone.currentSystemDefault())

    /* -------------------- Time calculations -------------------- */

    fun currentHour(): Int = now().hour

    fun currentMinute(): Int = now().minute

    fun minutesFromMidnight(): Int {
        val time = now()
        return time.hour * 60 + time.minute
    }

    fun currentDayOfWeek(): DayOfWeek =
        now().dayOfWeek

    fun allDaysOfWeek(): List<DayOfWeek> =
        DayOfWeek.entries

    /* -------------------- Formatting helpers -------------------- */

    fun formatMinutesTo12Hour(
        minutesFromMidnight: Int,
        uppercase: Boolean = true
    ): String {
        try {
            val normalizedMinutes =
                if (minutesFromMidnight >= 1440) 0 else minutesFromMidnight

            require(normalizedMinutes in 0 until 1440) {
                "Minutes must be between 0 and 1440"
            }

            val hour24 = normalizedMinutes / 60
            val minute = normalizedMinutes % 60

            val period = if (hour24 < 12) "AM" else "PM"
            val hour12 = when {
                hour24 == 0 -> 12
                hour24 > 12 -> hour24 - 12
                else -> hour24
            }

            val suffix = if (uppercase) period else period.lowercase(Locale.getDefault())

            return String.format(
                Locale.getDefault(),
                "%d:%02d %s",
                hour12,
                minute,
                suffix
            )
        } catch (e: Exception) {
            Timber.e(e, "Error while formatting minutes to 12 hour")
            return ""
        }
    }


    fun amPmForHour(hour24: Int): String {
        require(hour24 in 0..23)
        return if (hour24 < 12) "AM" else "PM"
    }

    fun dayName(
        dayOfWeek: DayOfWeek,
        short: Boolean = false,
        locale: Locale = Locale.getDefault()
    ): String {
        val name = dayOfWeek.name.lowercase(locale).replaceFirstChar {
            it.titlecase(locale)
        }
        return if (short) name.take(3) else name
    }
}
