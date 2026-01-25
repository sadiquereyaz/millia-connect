package com.reyaz.feature.attendance.domain.model

data class ScheduleLectureModel(
    val attendanceId: Long,
    val attendanceStatus: AttendanceStatus?,
    val endTimeMinute: Int,
    val lectureId: Long,
    val locationName: String?,
    val presentClasses: Int,
    val startTimeMinute: Int,
    val subjectName: String,
    val totalClasses: Int,
)