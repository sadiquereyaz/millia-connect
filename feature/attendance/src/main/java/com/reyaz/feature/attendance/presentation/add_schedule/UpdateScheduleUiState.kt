package com.reyaz.feature.attendance.presentation.add_schedule

import com.reyaz.feature.attendance.data.local.model.LectureWithSubject
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import kotlinx.datetime.DayOfWeek

data class UpdateScheduleUiState(
    val selectedDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val subjects: List<SubjectEntity> = emptyList(),
    val selectedSubjectId: Long? = null,
    val startTimeMinutes: Int = 540, // 9:00 AM
    val endTimeMinutes: Int = 600, // 10:00 AM
    val automationEnabled: Boolean = true,
    val locationId: Long = 0L,
    val locationName: String = "",
    val currentLocationCoordinates: String? = null,
    val lecturesForDay: List<LectureWithSubject> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
) {
    val selectedSubject: SubjectEntity?
        get() = subjects.firstOrNull { it.subjectId == selectedSubjectId }
}