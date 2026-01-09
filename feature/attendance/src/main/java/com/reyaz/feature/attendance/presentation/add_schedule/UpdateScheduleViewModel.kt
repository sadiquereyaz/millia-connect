package com.reyaz.feature.attendance.presentation.add_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.location.api.LocationProvider
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.data.local.model.LectureSlotEntity
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.LectureItem
import com.reyaz.feature.attendance.domain.repo.ScheduleRepository
import com.reyaz.feature.attendance.utils.TimeUtils.getCurrentDayOfWeek
import com.reyaz.feature.attendance.utils.TimeUtils.getHourInMinutesFromMidNight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import timber.log.Timber

class UpdateScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpdateScheduleUiState())
    val uiState: StateFlow<UpdateScheduleUiState> = _uiState.asStateFlow()

    init {
        loadSubjects()
        loadLocations()
        onDaySelect(dayIndex = getCurrentDayOfWeek())
    }

    fun onDaySelect(dayIndex: DayOfWeek) {
        viewModelScope.launch {
            _uiState.value = UpdateScheduleUiState(
                selectedDayOfWeek = dayIndex,
                selectedSubjectId = uiState.value.selectedSubjectId,
                subjects = uiState.value.subjects,
                isLoading = false,
            )
            _uiState.value.selectedDayOfWeek?.let { dayOfWeek ->
                scheduleRepository.observeLecturesWithSubjectForDay(
                    dayOfWeek
                ).collect { lecturesWithAttendanceSubject ->
                    _uiState.update { state ->
                        state.copy(
                            lecturesForDay = lecturesWithAttendanceSubject,
                        )
                    }
                }
            }
        }
    }

    fun onSubjectSelected(subjectId: Long) {
        _uiState.update { it.copy(selectedSubjectId = subjectId,) }
        val currStartMin = uiState.value.startTimeMinutes
        if (currStartMin == null) {
            onStartTimeChanged(
                startMin = getHourInMinutesFromMidNight(),
                isAutoUpdating = true,
            )
        }
    }

    fun onStartTimeChanged(startMin: Int, isAutoUpdating: Boolean = false) {
        viewModelScope.launch {
            val lecToCheck = uiState.value.lecturesForDay
                .filter { it.lecture.lectureId != uiState.value.selectedLectureId }

            // Check if start time falls within any existing lecture
            lecToCheck.forEach { lec ->
                val lecStartMin = lec.lecture.startTimeMinutes
                val lecEndMin = lec.lecture.startTimeMinutes

                // Conflict if new start time is within existing lecture
                // startMin >= lecStart AND startMin < lecEnd
                if (startMin >= lecStartMin && startMin < lecEndMin) {
                    // conflict detected
                    if (isAutoUpdating) {
                        _uiState.update {
                            it.copy()
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                startTimeMinutes = startMin,
                                isStartTimeError = true,
                                errorMessage = "Start time conflicts with \"${lec.subject.name}\"",
                                conflictLecId = lec.lecture.lectureId,
                            )
                        }
                    }
                    return@launch
                }
            }

            // No conflict - update start time
            _uiState.update {
                it.copy(
                    startTimeMinutes = startMin,
                )
            }
            onEndTimeChanged(startMin + 60, true)
        }
    }

    fun onEndTimeChanged(endMin: Int, isAutoUpdating: Boolean = false) {
        uiState.value.startTimeMinutes?.let { startMin ->
            if (endMin <= startMin) {
                _uiState.update {
                    it.copy(
                        endTimeMinutes = endMin,
                        isEndTimeError = true,
                        errorMessage = "End time must be after start time",
                    )
                }
                return
            }
            val lecToCheck = uiState.value.lecturesForDay
                .filter { it.lecture.lectureId != uiState.value.selectedLectureId }
            // Check for overlaps with existing lectures
            lecToCheck.forEach { lec ->
                val lecStartMin = lec.lecture.startTimeMinutes
                val lecEndMin = lec.lecture.endTimeMinutes

                // Two intervals [startMin, endMin] and [lecStart, lecEnd] overlap if:
                // startMin < lecEnd AND endMin > lecStart
                val hasOverlap = startMin < lecEndMin && endMin > lecStartMin

                if (hasOverlap) {
                    // conflict detected
                    if (isAutoUpdating) {
                        _uiState.update {
                            it.copy()
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                endTimeMinutes = endMin,
                                isEndTimeError = true,
                                errorMessage = "Time overlaps with ${lec.subject.name}",
                                conflictLecId = lec.lecture.lectureId,
                            )
                        }
                    }
                    return
                }
            }

            // No conflict - update end time
            _uiState.update {
                it.copy(
                    endTimeMinutes = endMin,
                )
            }
        }
    }

    fun clearUiMessages() {
        _uiState.update { it.copy() }
    }

    fun onAutomationSelected(index: Int) {
        _uiState.update { it.copy(automationSegSelectedIndex = index,) }
    }

    fun onUpdateLocationPermission(isGranted: Boolean) {
        _uiState.update { it.copy(isLocationPermissionGranted = isGranted,) }
    }

    fun onLocationSelected(locationId: Long) {
        _uiState.update { it.copy(selectedLocationId = locationId,) }
    }

    fun onLocationPicked(lat: Double?, lng: Double?) {
        val latLong = Pair(lat ?: 28.56180232032942, lng ?: 77.2814836859149)
        _uiState.update { it.copy(pickedLocationCoordinates = latLong,) }
    }

    private fun loadLocations() {
        scheduleRepository.observeAllLocations()
            .onEach { locations ->
                _uiState.update { it.copy(locationList = locations,) }
            }
            .launchIn(viewModelScope)
    }

    fun addNewLocation(locationName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy() }

            try {
                uiState.value.pickedLocationCoordinates?.let {
                    val location = LocationEntity(
                        locationName = locationName,
                        latitude = it.first,
                        longitude = it.second
                    )
                    val newLocationId = scheduleRepository.insertLocation(location)

                    _uiState.update {
                        it.copy(
                            selectedLocationId = newLocationId,
                            isLoading = false,
                            successMessage = "Location added successfully",
                        )
                    }
                } ?: run {
                    throw Exception("Location coordinates not available")
                }
            } catch (e: Exception) {
                Timber.e(e, "Error while adding new location")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to add location: ${e.message}",
                    )
                }
            }
        }
    }

    fun onEditLectureSlot(lecture: LectureAttendanceWithSubject) {
        _uiState.update {
            it.copy(
                selectedLectureId = lecture.lecture.lectureId,
                selectedSubjectId = lecture.subject.subjectId,
                startTimeMinutes = lecture.lecture.startTimeMinutes,
                endTimeMinutes = lecture.lecture.endTimeMinutes,
                selectedLocationId = lecture.lecture.locationId,
            )
        }
    }

    fun deleteLectureSlot(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy() }

            try {
//                scheduleRepository.deleteLectureSlot(lecture.lecture)

                _uiState.update { currUiState ->
                    currUiState.copy(
                        lecturesForDay = currUiState.lecturesForDay.filter { it.lecture.lectureId != id },
                        isLoading = false,
                        successMessage = "Lecture deleted successfully",
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error while deleting lecture")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to delete lecture: ${e.message}",
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy()
        }
    }

    fun loadSubjects() {
        scheduleRepository.observeAllSubjects()
            .onEach { subjects ->
                _uiState.update { it.copy(subjects = subjects,) }
            }
            .launchIn(viewModelScope)
    }

    fun addNewSubject(subjectName: String) {
        viewModelScope.launch {
            if (subjectName.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Subject name cannot be empty",) }
                return@launch
            }

            _uiState.update { it.copy() }

            try {
                val subject = SubjectEntity(name = subjectName)
                val subjectId = scheduleRepository.insertSubject(subject)

                _uiState.update {
                    it.copy(
                        selectedSubjectId = subjectId,
                        isLoading = false,
                        successMessage = "Subject added successfully",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to add subject: ${e.message}",
                    )
                }
            }
        }
    }

    fun saveLectureSlot() {
        viewModelScope.launch {
            val state = _uiState.value

            if (state.selectedSubjectId == null) {
                _uiState.update { it.copy(errorMessage = "Please select a subject",) }
                return@launch
            }

            _uiState.update { it.copy() }

            try {
                val lectureSlot = LectureSlotEntity(
                    lectureId = state.selectedLectureId ?: 0,
                    subjectId = state.selectedSubjectId,
                    locationId = state.selectedLocationId!!,        // todo:
                    dayOfWeek = state.selectedDayOfWeek!!.ordinal + 1,
                    startTimeMinutes = state.startTimeMinutes!!,
                    endTimeMinutes = state.endTimeMinutes!!
                )

                scheduleRepository.insertLectureSlot(lectureSlot)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Lecture saved successfully",
                        shouldNavigateBack = true,
                        // Reset form
                        selectedLocationId = null,
                        startTimeMinutes = null,
                        endTimeMinutes = null,
                        conflictLecId = null,
                        isStartTimeError = false,
                        isEndTimeError = false,
                        pickedLocationCoordinates = null,
                        automationSegSelectedIndex = null
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error while saving lecture")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to save lecture: ${e.message}",
                    )
                }
            }
        }
    }
}
