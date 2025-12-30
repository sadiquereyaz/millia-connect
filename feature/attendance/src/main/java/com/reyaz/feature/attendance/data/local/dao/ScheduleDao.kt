package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceUiModel
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.data.local.model.LectureWithSubject
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Transaction
    @Query("""
        SELECT * FROM lecture_slots
        WHERE dayOfWeek = :dayOfWeek
        ORDER BY startTimeMinutes
    """)
    fun observeLecturesWithSubject(
        dayOfWeek: Int
    ): Flow<List<LectureWithSubject>>

    @Transaction
    @Query("""
        SELECT
            ls.lectureId,
            ls.subjectId,
            ls.dayOfWeek,
            ls.startTimeMinutes,
            ls.endTimeMinutes,
            s.subjectId as subject_subjectId,
            s.name as subject_name,
            a.attendanceId,
            a.lectureId as attendance_lectureId,
            a.date,
            a.status
        FROM lecture_slots ls
        INNER JOIN subjects s ON ls.subjectId = s.subjectId
        LEFT JOIN attendance a ON ls.lectureId = a.lectureId AND a.date = :epochDay
        WHERE ls.dayOfWeek = :dayOfWeek
        ORDER BY ls.startTimeMinutes
    """)
    fun observeLectureAttendanceForDate(
        dayOfWeek: Int,
        epochDay: Long
    ): Flow<List<LectureAttendanceWithSubject>>
}
