package com.reyaz.feature.attendance.utils.time

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getHourInMinutesFromMidNight(): Int {
    val now = Clock.System.now()
        .toLocalDateTime(TimeZone.Companion.currentSystemDefault())

    return now.hour * 60
}