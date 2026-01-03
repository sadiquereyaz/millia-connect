package com.reyaz.feature.attendance.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class LectureAttendanceWithSubject(
    @Embedded val lecture: LectureSlotEntity,

    @Relation(
        parentColumn = "subjectId",
        entityColumn = "subjectId"
    )
    val subject: SubjectEntity,

    @Relation(
        parentColumn = "lectureId",
        entityColumn = "lectureId"
    )
    val attendance: AttendanceEntity?,

    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationId"
    )
    val location: LocationEntity?
)
