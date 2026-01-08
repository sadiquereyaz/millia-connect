package com.reyaz.feature.attendance.presentation.add_schedule

import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.LectureItem
import com.reyaz.feature.attendance.domain.model.LocationModel
import kotlinx.datetime.DayOfWeek

data class UpdateScheduleUiState(

    val selectedDayOfWeek: DayOfWeek? = null,

    val selectedSubjectId: Long? = null,
    val subjects: List<SubjectEntity> = emptyList(),

    val lecturesForDay: List<LectureItem> = emptyList(),

    val startTimeMinutes: Int? = null,
    val isStartTimeError: Boolean = false,

    val endTimeMinutes: Int? = null,
    val isEndTimeError: Boolean = false,

    val automationSegSelectedIndex: Int? = null,
    val isLocationPermissionGranted: Boolean = false,
    val currentLocationCoordinates: String? = null,
    val selectedLocationId: Long? = null,
    val pickedLocationCoordinates: Pair<Double, Double>? = null,
    val locationList: List<LocationModel> = emptyList(),

    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val conflictLecId: Long? = null,
//    val locationList: List<LocationModel> = emptyList(),
) {
    val selectedSubject: SubjectEntity?
        get() = subjects.firstOrNull { it.subjectId == selectedSubjectId }

    val isEndTimeFieldVisible: Boolean =
        !isStartTimeError && startTimeMinutes != null

    val hasValidTimeRange: Boolean =
        startTimeMinutes != null &&
                endTimeMinutes != null &&
                !isStartTimeError &&
                !isEndTimeError

    val isLocationRequired: Boolean =
        automationSegSelectedIndex == 0

    val hasValidLocation: Boolean =
        !isLocationRequired || selectedLocationId != null

    val isSaveEnabled: Boolean =
        selectedDayOfWeek != null &&
                selectedSubject != null &&
                hasValidTimeRange &&
                hasValidLocation &&
                conflictLecId == null &&
                automationSegSelectedIndex != null &&
                selectedLocationId != null &&
                !isLoading
}