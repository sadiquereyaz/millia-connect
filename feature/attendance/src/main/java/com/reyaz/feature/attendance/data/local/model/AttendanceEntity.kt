package com.reyaz.feature.attendance.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.reyaz.feature.attendance.domain.model.AttendanceStatus

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val attendanceId: Long = 0L,
    val lectureId: Long,
    val date: Int,
    var status: AttendanceStatus? = null
)
