package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceUiModel
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
        SELECT ls.* FROM lecture_slots ls
        WHERE ls.dayOfWeek = :dayOfWeek
        ORDER BY ls.startTimeMinutes
    """)
    fun observeLectureAttendanceForDate(
        dayOfWeek: Int,
        epochDay: Long
    ): Flow<List<LectureAttendanceUiModel>>
}
