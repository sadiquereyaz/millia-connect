package com.reyaz.feature.attendance.domain.model

import com.reyaz.feature.attendance.utils.time.minutesToAmPmString

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
    val timeText: String = "${minutesToAmPmString(startTimeMinute)}\n—\n${minutesToAmPmString(endTimeMinute)}"
}