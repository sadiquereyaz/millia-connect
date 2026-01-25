package com.reyaz.feature.attendance.domain.model

import com.reyaz.feature.attendance.utils.TimeUtils.formatMinutesTo12Hour

data class ScheduleLectureUiModel(
    val attendanceId: Long,
    val attendancePercentage: Int,
    val attendanceStatus: AttendanceStatus?,
    val attendanceWarning: String,
    val endTimeMinute: Int,
    val lectureId: Long,
    val locationName: String?,
    val startTimeMinute: Int,
    val subjectName: String,
){
    val timeText: String = "${formatMinutesTo12Hour(startTimeMinute)}\n—\n${formatMinutesTo12Hour(endTimeMinute)}"
}
