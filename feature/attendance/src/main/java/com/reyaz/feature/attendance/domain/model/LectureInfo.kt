package com.reyaz.feature.attendance.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class LectureInfo(
    val id: Int = 0,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val subjectName: String,
    val locationName: String,
    val attendancePer: Int,
//    val task: TaskItem,
    val reminder: List<LocalDateTime>,
    val attendanceStatus: AttendanceStatus,
) {
    val attendanceWarning: String = "You can miss this lecture"
    val isHappening: Boolean = true
}