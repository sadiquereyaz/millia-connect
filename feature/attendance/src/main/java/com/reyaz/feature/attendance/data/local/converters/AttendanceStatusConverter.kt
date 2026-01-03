package com.reyaz.feature.attendance.data.local.converters

import androidx.room.TypeConverter
import com.reyaz.feature.attendance.domain.model.AttendanceStatus

class AttendanceStatusConverter {
    @TypeConverter
    fun fromStatus(status: AttendanceStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): AttendanceStatus =
        AttendanceStatus.valueOf(value)
}
