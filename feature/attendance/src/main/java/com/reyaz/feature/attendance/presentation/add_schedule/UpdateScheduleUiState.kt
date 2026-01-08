package com.reyaz.feature.attendance.presentation.add_schedule

import com.reyaz.feature.attendance.data.local.model.LectureWithSubject
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.LocationModel
import com.reyaz.feature.attendance.domain.model.dummyLocations
import com.reyaz.feature.attendance.utils.time.getCurrentDayOfWeek
import com.reyaz.feature.attendance.utils.time.getHourInMinutesFromMidNight
import kotlinx.datetime.DayOfWeek

data class UpdateScheduleUiState(
    val selectedDayOfWeek: DayOfWeek = getCurrentDayOfWeek(),
    val subjects: List<SubjectEntity> = emptyList(),
    val selectedSubjectId: Long? = null,
    val startTimeMinutes: Int = getHourInMinutesFromMidNight(),
    val endTimeMinutes: Int = startTimeMinutes + 60,
    val automationSegSelectedIndex: Int? = null,
    val locationName: String = "",
    val currentLocationCoordinates: String? = null,
    val lecturesForDay: List<LectureWithSubject> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val selectedLocationId: Long? = null,
//    val locationList: List<LocationModel> = emptyList(),
    val locationList: List<LocationModel> = dummyLocations
) {
    val selectedSubject: SubjectEntity?
        get() = subjects.firstOrNull { it.subjectId == selectedSubjectId }
}