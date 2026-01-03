package com.reyaz.feature.attendance.utils

import com.reyaz.feature.attendance.domain.model.AttendanceStatus

fun AttendanceStatus.toPresentType(): AttendanceStatus {
    return when (this) {
        AttendanceStatus.PRESENT -> AttendanceStatus.PRESENT
        AttendanceStatus.ABSENT -> AttendanceStatus.ABSENT
        AttendanceStatus.CANCELLED -> AttendanceStatus.CANCELLED
    }
}