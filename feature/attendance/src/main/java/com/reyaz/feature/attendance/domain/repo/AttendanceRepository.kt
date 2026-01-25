package com.reyaz.feature.attendance.domain.repo

import com.reyaz.feature.attendance.data.local.model.AttendanceRecord
import com.reyaz.feature.attendance.domain.model.AttendanceStatus

interface AttendanceRepository {
    suspend fun deleteAttendance(attendanceId: Long)
    suspend fun upsertAttendance(attendanceId: Long?, lectureId: Long, date: Int, status: AttendanceStatus)
    suspend fun getAttendanceRecord(): List<AttendanceRecord>
}
