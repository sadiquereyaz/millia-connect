package com.reyaz.feature.attendance.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lecture")
data class LectureEntity(
    @PrimaryKey(autoGenerate = true)
    val lectureId: Long = 0L,           // todo: ensure that deleting lecture doesn't mean deleting all its related data. explore cascading
    val subjectId: Long,
    val locationId: Long?,
    val dayOfWeek: Int,
//    val isRepeatReminder: Boolean,
    val startTimeMinute: Int,
    val endTimeMinute: Int,
)