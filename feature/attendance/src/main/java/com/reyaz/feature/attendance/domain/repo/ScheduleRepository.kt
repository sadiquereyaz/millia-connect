package com.reyaz.feature.attendance.domain.repo

import com.reyaz.feature.attendance.data.local.model.AttendanceRecord
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.data.local.model.LectureSlotEntity
import com.reyaz.feature.attendance.data.local.model.LectureWithSubject
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.domain.model.LocationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

interface ScheduleRepository {
    fun observeLecturesForDate(date: LocalDate): Flow<List<LectureAttendanceWithSubject>>
    fun observeLecturesWithSubjectForDay(dayOfWeek: DayOfWeek): Flow<List<LectureAttendanceWithSubject>>

    suspend fun insertLectureSlot(slot: LectureSlotEntity): Long
    suspend fun deleteLectureSlot(lectureId: Long)

    suspend fun insertSubject(subject: SubjectEntity): Long
    fun observeAllSubjects(): Flow<List<SubjectEntity>>

    suspend fun insertLocation(location: LocationEntity): Long
    fun observeAllLocations(): Flow<List<LocationModel>>

    suspend fun upsertLectureSlotAttendanceForDate(attendanceId: Long?, lectureId: Long, date: Int, status: AttendanceStatus): Long
    suspend fun getAttendanceRecord(): List<AttendanceRecord>

}
