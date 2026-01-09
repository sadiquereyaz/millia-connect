package com.reyaz.feature.attendance.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lecture_slots",
    indices = [
        Index("subjectId"),
        Index("dayOfWeek"),
        Index("locationId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["subjectId"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LectureSlotEntity(
    @PrimaryKey(autoGenerate = true)
    val lectureId: Long = 0L,

    val subjectId: Long,
    
    val locationId: Long,

    // 1 = Monday … 7 = Sunday
    val dayOfWeek: Int,

//    val isRepeatReminder: Boolean,

    // minutes since midnight (optimized & indexable)
    val startTimeMinutes: Int,      // todo: rename to startTimeMinute (remove s)
    val endTimeMinutes: Int ///  // todo: rename to endTimeMinute (remove s)
)