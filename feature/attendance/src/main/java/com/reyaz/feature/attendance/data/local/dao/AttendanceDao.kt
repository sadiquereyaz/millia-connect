package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reyaz.feature.attendance.data.local.model.AttendanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAttendance(attendance: AttendanceEntity)

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
}
