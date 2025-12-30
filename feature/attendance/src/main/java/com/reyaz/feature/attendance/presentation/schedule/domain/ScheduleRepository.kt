package com.reyaz.feature.attendance.presentation.schedule.domain

import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.data.local.model.LectureSlotEntity
import com.reyaz.feature.attendance.data.local.model.LectureWithSubject
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

interface ScheduleRepository {
    fun observeLecturesForDate(date: LocalDate): Flow<List<LectureAttendanceWithSubject>>
    fun observeLecturesWithSubjectForDay(dayOfWeek: DayOfWeek): Flow<List<LectureWithSubject>>
    fun observeAllSubjects(): Flow<List<SubjectEntity>>
    suspend fun insertLectureSlot(slot: LectureSlotEntity): Long
    suspend fun deleteLectureSlot(slot: LectureSlotEntity)
    suspend fun insertSubject(subject: SubjectEntity): Long
}