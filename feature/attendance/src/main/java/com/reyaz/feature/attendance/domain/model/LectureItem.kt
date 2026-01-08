package com.reyaz.feature.attendance.domain.model

import com.reyaz.feature.attendance.utils.TimeUtils.minutesToAmPmString

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

val dummyLectures = listOf(
    LectureItem(
        id = 1L,
        startTimeMinute = 540, // 09:00 AM
        endTimeMinute = 600,   // 10:00 AM
        title = "Advanced Mathematics",
        location = "Room 302, Science Block",
        warning = "Low attendance alert",
        percentage = 65,
        status = AttendanceStatus.PRESENT
    ),
    LectureItem(
        id = 2L,
        startTimeMinute = 615, // 10:15 AM
        endTimeMinute = 675,   // 11:15 AM
        title = "Data Structures & Algorithms",
        location = "Lab 1, CS Dept",
        warning = "",
        percentage = 85,
        status = AttendanceStatus.NOT_COUNTED
    ),
    LectureItem(
        id = 3L,
        startTimeMinute = 720, // 12:00 PM
        endTimeMinute = 780,   // 01:00 PM
        title = "Digital Electronics",
        location = "Seminar Hall B",
        warning = "Critical! Next miss will drop % below 75",
        percentage = 76,
        status = AttendanceStatus.ABSENT
    ),
    LectureItem(
        id = 4L,
        startTimeMinute = 840, // 02:00 PM
        endTimeMinute = 900,   // 03:00 PM
        title = "Professional Communication",
        location = null,       // Example of null location
        warning = "",
        percentage = 92,
        status = AttendanceStatus.PRESENT
    ),
    LectureItem(
        id = 5L,
        startTimeMinute = 915, // 03:15 PM
        endTimeMinute = 975,   // 04:15 PM
        title = "Machine Learning Workshop",
        location = "Online - Zoom",
        warning = "",
        percentage = 100,
        status = AttendanceStatus.NOT_COUNTED
    )
)
