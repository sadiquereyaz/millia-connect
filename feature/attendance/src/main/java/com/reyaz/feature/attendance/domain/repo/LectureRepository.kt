package com.reyaz.feature.attendance.domain.repo

import com.reyaz.feature.attendance.data.local.model.LectureEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.EditScheduleLectureModel
import com.reyaz.feature.attendance.domain.model.ScheduleLectureUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

interface LectureRepository {
    suspend fun deleteLectureSlot(lectureId: Long)
    suspend fun upsertLecture(slot: LectureEntity): Long
    fun observeLecturesWithAttendance(date: LocalDate): Flow<List<ScheduleLectureUiModel>>
    fun observeLecturesForDay(dayOfWeek: DayOfWeek): Flow<List<EditScheduleLectureModel>>

    fun observeAllSubjects(): Flow<List<SubjectEntity>>
    suspend fun upsertSubject(subject: SubjectEntity): Long
}
