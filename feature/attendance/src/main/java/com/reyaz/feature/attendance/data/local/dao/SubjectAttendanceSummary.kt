package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class SubjectAttendanceSummary(
    val subjectId: Long,
    val totalClasses: Int,
    val presentClasses: Int
)

@Dao
interface AttendanceSummaryDao {

    @Query("""
        SELECT 
            ls.subjectId AS subjectId,
            COUNT(*) AS totalClasses,
            SUM(a.status = 'PRESENT') AS presentClasses
        FROM attendance a
        INNER JOIN lecture_slots ls ON a.lectureId = ls.lectureId
        GROUP BY ls.subjectId
    """)
    fun observeAttendanceSummary(): Flow<List<SubjectAttendanceSummary>>
}
