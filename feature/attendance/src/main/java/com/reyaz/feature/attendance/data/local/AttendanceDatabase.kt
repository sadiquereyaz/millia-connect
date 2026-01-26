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

/*
INSERT INTO subject (subjectId, subjectName) VALUES
(1, 'Mathematics'),
(2, 'Physics'),
(3, 'Chemistry'),
(4, 'English'),
(5, 'Computer Science');

INSERT INTO lecture (
    lectureId,
    subjectId,
    locationId,
    dayOfWeek,
    startTimeMinute,
    endTimeMinute
) VALUES
(1, 1, NULL, 1, 540, 600),
(2, 2, NULL, 2, 600, 660),
(3, 3, NULL, 3, 540, 600),
(4, 4, NULL, 4, 660, 720),
(5, 5, NULL, 5, 540, 600);

*/