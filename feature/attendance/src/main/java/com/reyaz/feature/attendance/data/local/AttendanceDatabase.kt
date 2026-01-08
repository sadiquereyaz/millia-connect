package com.reyaz.feature.attendance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.reyaz.feature.attendance.data.local.converters.AttendanceStatusConverter
import com.reyaz.feature.attendance.data.local.dao.AttendanceDao
import com.reyaz.feature.attendance.data.local.dao.AttendanceSummaryDao
import com.reyaz.feature.attendance.data.local.dao.LectureSlotDao
import com.reyaz.feature.attendance.data.local.dao.LocationDao
import com.reyaz.feature.attendance.data.local.dao.ScheduleDao
import com.reyaz.feature.attendance.data.local.dao.SubjectDao
import com.reyaz.feature.attendance.data.local.model.AttendanceEntity
import com.reyaz.feature.attendance.data.local.model.LectureSlotEntity
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity

@Database(
    entities = [
        SubjectEntity::class,
        LectureSlotEntity::class,
        AttendanceEntity::class,
        LocationEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(AttendanceStatusConverter::class)
abstract class AttendanceDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun locationDao(): LocationDao
    abstract fun lectureSlotDao(): LectureSlotDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun attendanceSummaryDao(): AttendanceSummaryDao

    companion object {
        const val DATABASE_NAME = "attendance_database"
    }
}