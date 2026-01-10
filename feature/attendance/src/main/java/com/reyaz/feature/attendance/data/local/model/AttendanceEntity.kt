package com.reyaz.feature.attendance.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.reyaz.feature.attendance.domain.model.AttendanceStatus

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

    val date: Int,     // Store as epochDay (LocalDate.toEpochDay())

    var status: AttendanceStatus
)
