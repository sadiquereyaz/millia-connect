package com.reyaz.feature.attendance.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attendance",
    indices = [
        Index("date"),
        Index("lectureId"),
        Index(value = ["lectureId", "date"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = LectureSlotEntity::class,
            parentColumns = ["lectureId"],
            childColumns = ["lectureId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val attendanceId: Long = 0L,

    val lectureId: Long,

    // Store as epochDay (LocalDate.toEpochDay())
    val date: Long,

    val status: AttendanceStatus
)
