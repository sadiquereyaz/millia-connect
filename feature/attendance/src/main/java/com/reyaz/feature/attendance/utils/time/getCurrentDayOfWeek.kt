package com.reyaz.feature.attendance.utils.time

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getCurrentDayOfWeek(): DayOfWeek {
    return Clock.System.now()
        .toLocalDateTime(TimeZone.Companion.currentSystemDefault())
        .dayOfWeek
}