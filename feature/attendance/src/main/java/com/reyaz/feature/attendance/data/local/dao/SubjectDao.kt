package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Upsert
    suspend fun upsertSubject(subject: SubjectEntity): Long

    @Query("SELECT * FROM subject ORDER BY subjectName ASC")
    fun observeSubjects(): Flow<List<SubjectEntity>>

    @Delete
    suspend fun deleteSubject(subject: SubjectEntity) : Int
}
