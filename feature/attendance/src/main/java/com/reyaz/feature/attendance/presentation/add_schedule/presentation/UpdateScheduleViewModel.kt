package com.reyaz.feature.attendance.presentation.add_schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.location.api.LocationProvider
import com.reyaz.core.location.model.MyLocationResult
import com.reyaz.feature.attendance.data.local.model.LectureSlotEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.repo.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek

class UpdateScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(UpdateScheduleUiState())
    val uiState: StateFlow<UpdateScheduleUiState> = _uiState.asStateFlow()

    init {
        loadSubjects()
        loadLecturesForSelectedDay()
    }

    fun onDaySelected(dayOfWeek: DayOfWeek) {
        _uiState.update { it.copy(selectedDayOfWeek = dayOfWeek) }
        loadLecturesForSelectedDay()
    }

    fun onSubjectSelected(subjectId: Long) {
        _uiState.update { it.copy(selectedSubjectId = subjectId) }
    }

    fun onStartTimeChanged(minutes: Int) {
        _uiState.update { it.copy(startTimeMinutes = minutes) }
    }

    fun onEndTimeChanged(minutes: Int) {
        _uiState.update { it.copy(endTimeMinutes = minutes) }
    }

    fun onAutomationToggled(enabled: Boolean) {
        _uiState.update { it.copy(automationEnabled = enabled) }
    }

    fun onLocationNameChanged(locationName: String) {
        _uiState.update { it.copy(locationName = locationName) }
    }

    suspend fun getCurrentLocation() {
        when (val result = locationProvider.getCurrentLocation()) {
            is MyLocationResult.Success -> {
                val coordinates = "${result.latitude},${result.longitude}"
                _uiState.update {
                    it.copy(
                        currentLocationCoordinates = coordinates,
                        locationName = "Current Location"
                    )
                }
            }
            is MyLocationResult.Error -> {
                _uiState.update {
                    it.copy(errorMessage = result.reason)
                }
            }
            MyLocationResult.LocationDisabled -> {
                _uiState.update {
                    it.copy(errorMessage = "Location is disabled")
                }
            }
            MyLocationResult.PermissionDenied -> {
                _uiState.update {
                    it.copy(errorMessage = "Location permission denied")
                }
            }
            MyLocationResult.Timeout -> {
                _uiState.update {
                    it.copy(errorMessage = "Location request timed out")
                }
            }
        }
    }

    fun saveLectureSlot() {
        viewModelScope.launch {
            val state = _uiState.value

            if (state.selectedSubjectId == null) {
                _uiState.update { it.copy(errorMessage = "Please select a subject") }
                return@launch
            }

            if (state.startTimeMinutes >= state.endTimeMinutes) {
                _uiState.update { it.copy(errorMessage = "End time must be after start time") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val lectureSlot = LectureSlotEntity(
                    subjectId = state.selectedSubjectId,
                    dayOfWeek = state.selectedDayOfWeek.value,
                    startTimeMinutes = state.startTimeMinutes,
                    endTimeMinutes = state.endTimeMinutes
                )

                scheduleRepository.insertLectureSlot(lectureSlot)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Lecture slot saved successfully",
                        // Reset form
                        selectedSubjectId = null,
                        startTimeMinutes = 540,
                        endTimeMinutes = 600,
                        locationName = ""
                    )
                }

                loadLecturesForSelectedDay()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to save lecture: ${e.message}"
                    )
                }
            }
        }
    }

    fun deleteLectureSlot(lecture: com.reyaz.feature.attendance.data.local.model.LectureWithSubject) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                scheduleRepository.deleteLectureSlot(lecture.lecture)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Lecture deleted successfully"
                    )
                }

                loadLecturesForSelectedDay()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to delete lecture: ${e.message}"
                    )
                }
            }
        }
    }

    fun addNewSubject(subjectName: String) {
        viewModelScope.launch {
            if (subjectName.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Subject name cannot be empty") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            try {
                val subject = SubjectEntity(name = subjectName)
                val subjectId = scheduleRepository.insertSubject(subject)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selectedSubjectId = subjectId,
                        successMessage = "Subject added successfully"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to add subject: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                successMessage = null
            )
        }
    }

    private fun loadSubjects() {
        scheduleRepository.observeAllSubjects()
            .onEach { subjects ->
                _uiState.update { it.copy(subjects = subjects) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadLecturesForSelectedDay() {
        scheduleRepository.observeLecturesWithSubjectForDay(_uiState.value.selectedDayOfWeek)
            .onEach { lectures ->
                _uiState.update { it.copy(lecturesForDay = lectures) }
            }
            .launchIn(viewModelScope)
    }
}