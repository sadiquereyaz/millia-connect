package com.reyaz.feature.attendance.data.repository

import com.reyaz.feature.attendance.data.local.dao.AttendanceDao
import com.reyaz.feature.attendance.data.local.dao.LectureSlotDao
import com.reyaz.feature.attendance.data.local.dao.LocationDao
import com.reyaz.feature.attendance.data.local.dao.ScheduleDao
import com.reyaz.feature.attendance.data.local.dao.SubjectDao
import com.reyaz.feature.attendance.data.local.model.AttendanceEntity
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.data.local.model.LectureSlotEntity
import com.reyaz.feature.attendance.data.local.model.LectureWithSubject
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.domain.model.LocationModel
import com.reyaz.feature.attendance.domain.repo.ScheduleRepository
import com.reyaz.feature.attendance.utils.mapper.toLocationDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

class ScheduleRepositoryImpl(
    private val scheduleDao: ScheduleDao,
    private val lectureSlotDao: LectureSlotDao,
    private val subjectDao: SubjectDao,
    private val locationDao: LocationDao,
    private val attendanceDao: AttendanceDao
) : ScheduleRepository {

    override fun observeLecturesForDate(date: LocalDate): Flow<List<LectureAttendanceWithSubject>> {
        val dayOfWeek = date.dayOfWeek.value // Monday = 1, Sunday = 7
        val epochDay = date.toEpochDays()
        return scheduleDao.observeLectureAttendanceForDate(dayOfWeek, epochDay.toLong())
    }

    override fun observeLecturesWithSubjectForDay(dayOfWeek: DayOfWeek): Flow<List<LectureAttendanceWithSubject>> {
        return scheduleDao.observeLecturesWithSubject(dayOfWeek.value)
    }

    override fun observeAllSubjects(): Flow<List<SubjectEntity>> {
        return subjectDao.observeSubjects()
    }

    override suspend fun insertLectureSlot(slot: LectureSlotEntity): Long {
        return lectureSlotDao.insertLectureSlot(slot)
    }

    override suspend fun deleteLectureSlot(lectureId: Long) {
        lectureSlotDao.deleteLectureSlot(lectureId)
    }

    override suspend fun insertSubject(subject: SubjectEntity): Long {
        return subjectDao.insertSubject(subject)
    }

    override suspend fun insertLocation(location: LocationEntity): Long {
        return locationDao.insertLocation(location)
    }

    override fun observeAllLocations(): Flow<List<LocationModel>> {
        return locationDao.observeLocations().map { entities ->
            entities.map { it.toLocationDomain() }
        }
    }

    override suspend fun upsertLectureSlotAttendanceForDate(
        attendanceId: Long?,
        lectureId: Long,
        date: Int,
        status: AttendanceStatus
    ): Long {
        return attendanceDao.upsertAttendance(
            AttendanceEntity(
                attendanceId = attendanceId ?: 0L,
                lectureId = lectureId,
                date = date,
                status = status,
            )
        )
    }
}
