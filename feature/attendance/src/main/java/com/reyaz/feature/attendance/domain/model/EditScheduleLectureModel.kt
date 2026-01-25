package com.reyaz.feature.attendance.domain.model

data class EditScheduleLectureModel(
    val endTimeMinute: Int,
    val lectureId: Long,
    val locationId: Long?,
    val locationName: String?,
    val startTimeMinute: Int,
    val subjectName: String,
    val subjectId: Long,
)