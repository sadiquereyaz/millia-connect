package com.reyaz.feature.attendance.presentation.add_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.location.api.LocationProvider
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.data.local.model.LectureSlotEntity
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.repo.ScheduleRepository
import com.reyaz.feature.attendance.utils.TimeUtils
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
        onDaySelect(dayOfWeek = TimeUtils.currentDayOfWeek())
    }

    fun onDaySelect(dayOfWeek: DayOfWeek) {
        viewModelScope.launch {
            resetForm(
                selectedDayOfWeek = dayOfWeek,
                selectedSubjectId = uiState.value.selectedSubjectId
            )
            scheduleRepository.observeLecturesWithSubjectForDay(dayOfWeek).collect { slots ->
                _uiState.update { state ->
                    state.copy(
                        lecturesForDay = slots,
                    )
                }
            }
        }
    }

    fun onSubjectSelected(subjectId: Long) {
        resetForm(selectedSubjectId = subjectId)
    }

    private fun isStartTimeConflict(currStartMinutes: Int, ignoreLecId: Long? = null): Boolean {

        val isConflict = uiState.value.lecturesForDay.any { slot ->

            val lecture = slot.lecture

            val conflictedLecFound = lecture.lectureId != ignoreLecId &&
                    currStartMinutes >= lecture.startTimeMinutes &&
                    currStartMinutes < lecture.endTimeMinutes

            if (conflictedLecFound) {
                _uiState.update {
                    it.copy(
                        isStartTimeError = true,
                        errorMessage = "Start time conflicts with \"${slot.subject.name}\"",
                        conflictLecId = lecture.lectureId,
                    )
                }
            }

            conflictedLecFound
        }
        return isConflict
    }

    private fun isEndTimeConflict(currEndMinutes: Int, ignoreLecId: Long? = null): Boolean {

        val state = uiState.value

        state.startTimeMinutes?.let { selectedStartTime ->

            if (selectedStartTime >= currEndMinutes) {
                _uiState.update {
                    it.copy(
                        isEndTimeError = true,
                        errorMessage = "End time must be after start time",
                        conflictLecId = null,
                    )
                }
                return true
            }

            return uiState.value.lecturesForDay.any { slot ->

                val lecture = slot.lecture

                val isOverlapping = lecture.lectureId != ignoreLecId &&
                        currEndMinutes >= lecture.startTimeMinutes &&
                        currEndMinutes < lecture.endTimeMinutes

                if (isOverlapping) {
                    _uiState.update {
                        it.copy(
                            isEndTimeError = true,
                            errorMessage = "Time overlaps with ${state.selectedSubject?.name}",
                            conflictLecId = lecture.lectureId
                        )
                    }
                }
                isOverlapping
            }
        }
        return false
    }

    fun onStartTimeChanged(startMin: Int) {
        viewModelScope.launch {
            val lecToCheck = uiState.value.lecturesForDay
                .filter { it.lecture.lectureId != uiState.value.selectedLectureId }

            // Check if start time falls within any existing lecture
            lecToCheck.forEach { lec ->
                val lecStartMin = lec.lecture.startTimeMinutes
                val lecEndMin = lec.lecture.startTimeMinutes

                // Conflict if new start time is within existing lecture
                // startMin >= lecStart AND startMin < lecEnd
                if (startMin >= lecStartMin && startMin < lecEndMin) {  // conflict detected
                    Timber.d("Conflict detected")
                    _uiState.update {
                        it.copy(
                            startTimeMinutes = null,
                            endTimeMinutes = null,
                            isStartTimeError = true,
                            errorMessage = "Start time conflicts with \"${lec.subject.name}\"",
                            conflictLecId = lec.lecture.lectureId,
                        )
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
        _uiState.update {
            it.copy(
                errorMessage = null,
                successMessage = null,
            )
        }
    }

    fun onAutomationSelected(index: Int) {
        _uiState.update { it.copy(automationSegSelectedIndex = index) }
    }

    fun onUpdateLocationPermission(isGranted: Boolean) {
        _uiState.update { it.copy(isLocationPermissionGranted = isGranted) }
    }

    fun onLocationSelected(locationId: Long) {
        _uiState.update { it.copy(selectedLocationId = locationId) }
    }

    fun onLocationPicked(lat: Double?, lng: Double?) {
        val latLong = Pair(lat ?: 28.56180232032942, lng ?: 77.2814836859149)
        _uiState.update { it.copy(pickedLocationCoordinates = latLong) }
    }

    private fun loadLocations() {
        scheduleRepository.observeAllLocations()
            .onEach { locations ->
                _uiState.update { it.copy(locationList = locations) }
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

    fun onEditLectureSlot(slot: LectureAttendanceWithSubject) {
        val lecture = slot.lecture
        resetForm(
            selectedLectureId = lecture.lectureId,
            selectedSubjectId = slot.subject.subjectId,
            startTimeMinutes = lecture.startTimeMinutes,
            endTimeMinutes = lecture.endTimeMinutes,
            selectedLocationId = lecture.locationId,
            automationSegSelectedIndex = if (slot.location != null) 0 else 1
        )
    }

    fun deleteLectureSlot(id: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null,
                )
            }

            try {
                scheduleRepository.deleteLectureSlot(id)
                _uiState.update { currUiState ->
                    currUiState.copy(
//                        lecturesForDay = currUiState.lecturesForDay.filter { it.lecture.lectureId != id },
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

    fun loadSubjects() {
        scheduleRepository.observeAllSubjects()
            .onEach { subjects ->
                _uiState.update { it.copy(subjects = subjects) }
            }
            .launchIn(viewModelScope)
    }

    fun addNewSubject(subjectName: String) {
        viewModelScope.launch {
            try {
                val subject = SubjectEntity(name = subjectName)
                val subjectId = scheduleRepository.insertSubject(subject)

                onSubjectSelected(subjectId)

                _uiState.update {
                    it.copy(
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
            try {
                val state = uiState.value

                if (state.selectedSubjectId == null) {
                    _uiState.update { it.copy(errorMessage = "Please select a subject") }
                    return@launch
                }

                _uiState.update { it.copy(isLoading = true) }


                val lectureSlot = LectureSlotEntity(
                    lectureId = state.selectedLectureId ?: 0,
                    subjectId = state.selectedSubjectId,
                    locationId = state.selectedLocationId,
                    dayOfWeek = state.selectedDayOfWeek!!.value,
                    startTimeMinutes = state.startTimeMinutes!!,
                    endTimeMinutes = state.endTimeMinutes!!
                )

                scheduleRepository.insertLectureSlot(lectureSlot)

                resetForm(
                    selectedSubjectId = uiState.value.selectedSubjectId,
                    successMessage = "Lecture saved successfully",
                )

            } catch (e: Exception) {
                Timber.e(e, "Error while saving lecture")
                _uiState.update { it.copy(errorMessage = "Failed to save lecture: ${e.message}") }
            }
        }
    }

    private fun resetForm(
        selectedSubjectId: Long? = null,
        successMessage: String? = null,
        isLoading: Boolean = false,
        errorMessage: String? = null,
        selectedLectureId: Long? = null,
        startTimeMinutes: Int? = null,
        endTimeMinutes: Int? = null,
        selectedLocationId: Long? = null,
        selectedDayOfWeek: DayOfWeek = uiState.value.selectedDayOfWeek,
        automationSegSelectedIndex: Int = 0,
    ) {
        _uiState.update {
            it.copy(
                isLoading = isLoading,
                selectedDayOfWeek = selectedDayOfWeek,
                selectedLectureId = selectedLectureId,
                selectedSubjectId = selectedSubjectId,
                selectedLocationId = selectedLocationId,
                startTimeMinutes = startTimeMinutes,
                endTimeMinutes = endTimeMinutes,
                conflictLecId = null,
                isStartTimeError = false,
                isEndTimeError = false,
                pickedLocationCoordinates = null,
                automationSegSelectedIndex = automationSegSelectedIndex,
                errorMessage = errorMessage,
                successMessage = successMessage,
            )
        }
    }
}
