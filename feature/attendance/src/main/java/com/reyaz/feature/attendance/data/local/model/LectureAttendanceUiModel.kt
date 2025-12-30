package com.reyaz.feature.attendance.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class LectureAttendanceUiModel(
    @Embedded val lecture: LectureSlotEntity,

    @Relation(
        parentColumn = "lectureId",
        entityColumn = "lectureId"
    )
    val attendance: AttendanceEntity?
)
