package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.reyaz.feature.attendance.data.local.model.AttendanceEntity
import com.reyaz.feature.attendance.data.local.model.AttendanceRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Upsert
    suspend fun upsertAttendance(attendance: AttendanceEntity): Long

    @Query("""
        SELECT * FROM attendance
        WHERE date = :epochDay
    """)
    suspend fun getAttendanceForDate(epochDay: Long): List<AttendanceEntity>

    @Query("""
        SELECT * FROM attendance
        WHERE lectureId = :lectureId
        ORDER BY date DESC
    """)
    fun observeAttendanceForLecture(lectureId: Long): Flow<List<AttendanceEntity>>

    @Query("""
        DELETE FROM attendance
        WHERE lectureId = :lectureId AND date = :epochDay
    """)
    suspend fun deleteAttendance(lectureId: Long, epochDay: Long)

    @Query("""
    SELECT 
        subjects.subjectName AS subjectName,
        attendance.date AS date,
        CASE 
            WHEN attendance.status = 'PRESENT' THEN 1 
            ELSE 0 
        END AS isPresent
    FROM attendance
    INNER JOIN lecture_slots 
        ON attendance.lectureId = lecture_slots.lectureId
    INNER JOIN subjects 
        ON lecture_slots.subjectId = subjects.subjectId
""")
    suspend fun getAttendanceRecord(): List<AttendanceRecord>

}
