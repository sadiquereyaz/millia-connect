package com.reyaz.feature.attendance.data.repository

import com.reyaz.feature.attendance.data.local.dao.ScheduleDao
import com.reyaz.feature.attendance.data.local.dao.SubjectDao
import com.reyaz.feature.attendance.data.local.model.LectureEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.ScheduleLectureUiModel
import com.reyaz.feature.attendance.domain.model.EditScheduleLectureModel
import com.reyaz.feature.attendance.domain.repo.LectureRepository
import com.reyaz.feature.attendance.utils.calculateAttendancePercentage
import com.reyaz.feature.attendance.utils.generateAttendanceWarningMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

class LectureRepositoryImpl(
    private val scheduleDao: ScheduleDao,
    private val subjectDao: SubjectDao,
) : LectureRepository {


    override fun observeLecturesWithAttendance(
        date: LocalDate
    ): Flow<List<ScheduleLectureUiModel>> {

        val dayOfWeek = date.dayOfWeek.value
        val epochDay = date.toEpochDays()

        return scheduleDao
            .observeScheduleLectures(dayOfWeek, epochDay)
            .map { lectures ->

                lectures.map { lecture ->

                    val percentage = calculateAttendancePercentage(
                        presentClasses = lecture.presentClasses,
                        totalClasses = lecture.totalClasses
                    )

                    val message = generateAttendanceWarningMessage(
                        totalClassesHeld = lecture.totalClasses,
                        classesAttended = lecture.presentClasses,
                        targetPercentage = 75       // todo: make it dynamic
                    )

                    ScheduleLectureUiModel(
                        lectureId = lecture.lectureId,
                        attendanceId = lecture.attendanceId,
                        attendanceStatus = lecture.attendanceStatus,
                        startTimeMinute = lecture.startTimeMinute,
                        endTimeMinute = lecture.endTimeMinute,
                        subjectName = lecture.subjectName,
                        locationName = lecture.locationName,
                        attendancePercentage = percentage,
                        attendanceWarning = message
                    )
                }
            }
    }


    override fun observeLecturesForDay(dayOfWeek: DayOfWeek): Flow<List<EditScheduleLectureModel>> {
        return scheduleDao.observeLecturesWithSubject(dayOfWeek.value)
    }

    override suspend fun upsertLecture(slot: LectureEntity): Long {
        return scheduleDao.upsertSchedule(slot)
    }

    override suspend fun deleteLectureSlot(lectureId: Long) {
        scheduleDao.deleteLecture(lectureId)
    }

    override fun observeAllSubjects(): Flow<List<SubjectEntity>> {
        return subjectDao.observeSubjects()
    }

    override suspend fun upsertSubject(subject: SubjectEntity): Long {
        return subjectDao.upsertSubject(subject)
    }
}
