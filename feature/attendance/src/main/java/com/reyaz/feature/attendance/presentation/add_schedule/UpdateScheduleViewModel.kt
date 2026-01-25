package com.reyaz.feature.attendance.presentation.add_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.location.api.LocationProvider
import com.reyaz.feature.attendance.data.local.model.LectureEntity
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.EditScheduleLectureModel
import com.reyaz.feature.attendance.domain.repo.LectureRepository
import com.reyaz.feature.attendance.domain.repo.LocationRepository
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
    private val lectureRepository: LectureRepository,
    private val locationRepository: LocationRepository,
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
            lectureRepository.observeLecturesForDay(dayOfWeek).collect { slots ->
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

        val isConflict = uiState.value.lecturesForDay.any { lecture ->

            val conflictedLecFound = lecture.lectureId != ignoreLecId &&
                    currStartMinutes >= lecture.startTimeMinute &&
                    currStartMinutes < lecture.endTimeMinute

            if (conflictedLecFound) {
                _uiState.update {
                    it.copy(
                        isStartTimeError = true,
                        errorMessage = "Start time conflicts with \"${lecture.subjectName}\"",
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

            return uiState.value.lecturesForDay.any { lecture ->

                val isOverlapping = lecture.lectureId != ignoreLecId &&
                        currEndMinutes >= lecture.startTimeMinute &&
                        currEndMinutes < lecture.endTimeMinute

                if (isOverlapping) {
                    _uiState.update {
                        it.copy(
                            isEndTimeError = true,
                            errorMessage = "Time overlaps with ${state.selectedSubject?.subjectName}",
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
            if (!isStartTimeConflict(startMin)) {
                // no time conflict
                _uiState.update {
                    it.copy(
                        startTimeMinutes = startMin,
                        isStartTimeError = false,
                        errorMessage = null,
                        conflictLecId = null,
                        endTimeMinutes = null,
                    )
                }
            }
        }
    }

    fun onEndTimeChanged(endMin: Int, isAutoUpdating: Boolean = false) {
        viewModelScope.launch {
            if (!isEndTimeConflict(endMin)){
                _uiState.update {
                    it.copy(
                        endTimeMinutes = endMin,
                        isEndTimeError = false,
                        errorMessage = null,
                        conflictLecId = null,
                    )
                }
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
        locationRepository.observeAllLocations()
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
                    val newLocationId = locationRepository.insertLocation(location)

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

    fun onEditLectureSlot(lecture: EditScheduleLectureModel) {
        resetForm(
            selectedLectureId = lecture.lectureId,
            selectedSubjectId = lecture.subjectId,
            startTimeMinutes = lecture.startTimeMinute,
            endTimeMinutes = lecture.endTimeMinute,
            selectedLocationId = lecture.locationId,
            automationSegSelectedIndex = if (lecture.locationId != null) 0 else 1
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
                lectureRepository.deleteLectureSlot(id)
                _uiState.update { currUiState ->
                    currUiState.copy(
//                        lecturesForDay = currUiState.lecturesForDay.filter { it.lectureId != id },
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
        lectureRepository.observeAllSubjects()
            .onEach { subjects ->
                _uiState.update { it.copy(subjects = subjects) }
            }
            .launchIn(viewModelScope)
    }

    fun addNewSubject(subjectName: String) {
        viewModelScope.launch {
            try {
                val subject = SubjectEntity(subjectName = subjectName)
                val subjectId = lectureRepository.upsertSubject(subject)

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


                val lectureEntitySlot = LectureEntity(
                    lectureId = state.selectedLectureId ?: 0,
                    subjectId = state.selectedSubjectId,
                    locationId = state.selectedLocationId,
                    dayOfWeek = state.selectedDayOfWeek!!.value,
                    startTimeMinute = state.startTimeMinutes!!,
                    endTimeMinute = state.endTimeMinutes!!
                )

                lectureRepository.upsertLecture(lectureEntitySlot)

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
