package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.reyaz.feature.attendance.data.local.model.LectureEntity
import com.reyaz.feature.attendance.domain.model.ScheduleLectureModel
import com.reyaz.feature.attendance.domain.model.EditScheduleLectureModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("DELETE FROM lecture WHERE lectureId = :lectureId")
    suspend fun deleteLecture(lectureId: Long)

    @Query("""
        SELECT
            l.lectureId         AS lectureId,
            l.startTimeMinute   AS startTimeMinute,
            l.endTimeMinute     AS endTimeMinute,
            l.locationId        AS locationId,
            loc.locationName    AS locationName,
            s.subjectName       AS subjectName,
            l.subjectId       AS subjectId
        FROM lecture l
        INNER JOIN subject s
            ON s.subjectId = l.subjectId
        LEFT JOIN location loc
            ON loc.locationId = l.locationId
        WHERE l.dayOfWeek = :dayOfWeek
        ORDER BY l.startTimeMinute
    """)
    fun observeLecturesWithSubject(
        dayOfWeek: Int
    ): Flow<List<EditScheduleLectureModel>>

    @Query(
        """
        SELECT
            l.lectureId                                  AS lectureId,
            COALESCE(a.attendanceId, 0)                  AS attendanceId,
            a.status                                     AS attendanceStatus,
            l.startTimeMinute                            AS startTimeMinute,
            l.endTimeMinute                              AS endTimeMinute,
            s.subjectName                                AS subjectName,
            loc.locationName                             AS locationName,

            COUNT(attAll.attendanceId)                   AS totalClasses,
            SUM(attAll.status = 'PRESENT')               AS presentClasses

        FROM lecture l

        INNER JOIN subject s
            ON s.subjectId = l.subjectId

        LEFT JOIN location loc
            ON loc.locationId = l.locationId

        -- attendance for selected date
        LEFT JOIN attendance a
            ON a.lectureId = l.lectureId
           AND a.date = :epochDay

        -- attendance history (for percentage)
        LEFT JOIN attendance attAll
            ON attAll.lectureId = l.lectureId

        WHERE l.dayOfWeek = :dayOfWeek

        GROUP BY l.lectureId

        ORDER BY l.startTimeMinute ASC
    """
    )
    fun observeScheduleLectures(
        dayOfWeek: Int,
        epochDay: Int
    ): Flow<List<ScheduleLectureModel>>

    @Upsert
    suspend fun upsertSchedule(schedule: LectureEntity): Long
}