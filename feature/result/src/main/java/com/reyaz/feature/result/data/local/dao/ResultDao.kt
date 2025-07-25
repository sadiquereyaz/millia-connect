package com.reyaz.feature.result.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.reyaz.feature.result.data.local.dto.CourseWithList
import com.reyaz.feature.result.data.local.entities.CourseEntity
import com.reyaz.feature.result.data.local.entities.ResultListEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ResultDao {
    @Transaction
    @Query("SELECT * FROM CourseEntity")
    fun observeResults(): Flow<List<CourseWithList>>

    @Query("SELECT * FROM CourseEntity")
    suspend fun getCourses(): List<CourseEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourse(course: CourseEntity)

    @Query("SELECT * FROM ResultListEntity WHERE listOwnerId = :courseId")
    suspend fun getResults(courseId: String): List<ResultListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResultList(detail: ResultListEntity) : Long

    // update course tracking
    @Query("UPDATE CourseEntity SET trackEnabled = :status WHERE courseId = :courseId")
    suspend fun updateTrackingStatus(status: Boolean, courseId: String) : Int

    @Query("UPDATE ResultListEntity SET pdfPath = :path, downloadProgress = :progress WHERE listId = :listId")
    suspend fun updatePdfPath(path: String? = null, listId: String, progress: Int? = null)

    @Query("UPDATE ResultListEntity SET downloadProgress = :progress WHERE listId = :listId")
    suspend fun updateDownloadProgress(progress: Int? = null, listId: String)

    // delete course
    @Query("DELETE FROM CourseEntity WHERE courseId = :courseId")
    suspend fun deleteCourse(courseId: String) : Int

    @Query("SELECT EXISTS(SELECT 1 FROM ResultListEntity WHERE listOwnerId = :courseId)")
    suspend fun havePreviousResult(courseId: String): Boolean

    @Query("UPDATE ResultListEntity SET viewed = 1 WHERE listOwnerId = :courseId")
    suspend fun markCourseAsRead(courseId: String) : Int

    @Query("UPDATE CourseEntity SET lastSync = :lastSync WHERE courseId = :courseId")
    fun updateLastFetchedDate(courseId: String, lastSync: Long = Date().time)

}