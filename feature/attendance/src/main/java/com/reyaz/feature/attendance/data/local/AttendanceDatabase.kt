package com.reyaz.feature.attendance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.reyaz.feature.attendance.data.local.converters.AttendanceStatusConverter
import com.reyaz.feature.attendance.data.local.dao.AttendanceDao
import com.reyaz.feature.attendance.data.local.dao.LocationDao
import com.reyaz.feature.attendance.data.local.dao.ScheduleDao
import com.reyaz.feature.attendance.data.local.dao.SubjectDao
import com.reyaz.feature.attendance.data.local.model.AttendanceEntity
import com.reyaz.feature.attendance.data.local.model.LectureEntity
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity

@Database(
    entities = [
        SubjectEntity::class,
        LectureEntity::class,
        AttendanceEntity::class,
        LocationEntity::class
    ],
    version = 7,
    exportSchema = false
)
@TypeConverters(AttendanceStatusConverter::class)
abstract class AttendanceDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun locationDao(): LocationDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        const val DATABASE_NAME = "attendance_database"
    }
}