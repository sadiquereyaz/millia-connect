package com.reyaz.feature.attendance.data.local.model

data class AttendanceRecord(
    val subjectName: String,
    val date: Int,
    val isPresent: Boolean
)