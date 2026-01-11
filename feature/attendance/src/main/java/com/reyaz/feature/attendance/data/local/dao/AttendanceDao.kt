package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.reyaz.feature.attendance.data.local.model.AttendanceEntity
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
        WHERE attendanceId = :attendanceId
    """)
    suspend fun deleteAttendance(attendanceId: Long)
}
