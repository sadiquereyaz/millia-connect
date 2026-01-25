package com.reyaz.feature.attendance.data.repository

import com.reyaz.feature.attendance.data.local.dao.AttendanceDao
import com.reyaz.feature.attendance.data.local.dao.LocationDao
import com.reyaz.feature.attendance.data.local.dao.ScheduleDao
import com.reyaz.feature.attendance.data.local.dao.SubjectDao
import com.reyaz.feature.attendance.data.local.model.AttendanceEntity
import com.reyaz.feature.attendance.data.local.model.AttendanceRecord
import com.reyaz.feature.attendance.data.local.model.LectureEntity
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.domain.model.LocationModel
import com.reyaz.feature.attendance.domain.model.ScheduleLectureUiModel
import com.reyaz.feature.attendance.domain.repo.AttendanceRepository
import com.reyaz.feature.attendance.domain.repo.LectureRepository
import com.reyaz.feature.attendance.utils.mapper.toLocationDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import timber.log.Timber

class AttendanceRepositoryImpl(
    private val locationDao: LocationDao,
    private val attendanceDao: AttendanceDao
) : AttendanceRepository {

    override suspend fun deleteAttendance(attendanceId: Long) {
        attendanceDao.deleteAttendance(attendanceId = attendanceId)
    }

    override suspend fun upsertAttendance(
        attendanceId: Long?,
        lectureId: Long,
        date: Int,
        status: AttendanceStatus
    ) {
        attendanceDao.upsertAttendance(
            AttendanceEntity(
                attendanceId = attendanceId ?: 0L,
                lectureId = lectureId,
                date = date,
                status = status,
            )
        )
    }
    override suspend fun getAttendanceRecord(): List<AttendanceRecord> {
        return attendanceDao.getAttendanceRecord()
    }
}
