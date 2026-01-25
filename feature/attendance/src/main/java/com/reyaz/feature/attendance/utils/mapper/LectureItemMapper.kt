package com.reyaz.feature.attendance.utils.mapper

import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.domain.model.LectureItem

fun LectureAttendanceWithSubject.toDomain(): LectureItem {
    val sub = this.subject
    val lec = this.lecture
    val attendance = this.attendance
    return LectureItem(
        id = lec.lectureId,
        title = sub.subjectName,
        startTimeMinute = lec.startTimeMinutes,
        endTimeMinute = lec.endTimeMinutes,
        location = this.location?.locationName,
        warning = "this is warning text",
        percentage = 66,
        status = attendance?.status ?: AttendanceStatus.NOT_COUNTED,
    )
}