package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reyaz.feature.attendance.data.local.model.LectureSlotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LectureSlotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectureSlot(slot: LectureSlotEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectureSlots(slots: List<LectureSlotEntity>)

    @Query("""
        SELECT * FROM lecture_slots
        WHERE dayOfWeek = :dayOfWeek
        ORDER BY startTimeMinutes ASC
    """)
    fun observeLecturesForDay(dayOfWeek: Int): Flow<List<LectureSlotEntity>>

    @Query("""
        SELECT * FROM lecture_slots
        WHERE subjectId = :subjectId
        ORDER BY dayOfWeek, startTimeMinutes
    """)
    fun observeLecturesForSubject(subjectId: Long): Flow<List<LectureSlotEntity>>

    @Query("DELETE FROM lecture_slots WHERE lectureId = :lectureId")
    suspend fun deleteLectureSlot(lectureId: Long)

    @Query("DELETE FROM lecture_slots WHERE subjectId = :subjectId")
    suspend fun deleteLecturesForSubject(subjectId: Long)
}
