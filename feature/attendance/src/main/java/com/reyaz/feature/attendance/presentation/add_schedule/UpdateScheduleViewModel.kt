package com.reyaz.feature.attendance.presentation.add_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.location.api.LocationProvider
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.LectureItem
import com.reyaz.feature.attendance.domain.model.dummyLectures
import com.reyaz.feature.attendance.domain.repo.ScheduleRepository
import com.reyaz.feature.attendance.utils.TimeUtils.getCurrentDayOfWeek
import com.reyaz.feature.attendance.utils.TimeUtils.getHourInMinutesFromMidNight
import kotlinx.coroutines.delay
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
        loadLocations()
        loadLecturesForSelectedDay(dayIndex = getCurrentDayOfWeek())
    }

    fun onSubjectSelected(subjectId: Long) {
        _uiState.update { it.copy(selectedSubjectId = subjectId) }
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
            // Check if start time falls within any existing lecture
            uiState.value.lecturesForDay.forEach { lec ->
                val lecStartMin = lec.startTimeMinute
                val lecEndMin = lec.endTimeMinute

                // Conflict if new start time is within existing lecture
                // startMin >= lecStart AND startMin < lecEnd
                if (startMin >= lecStartMin && startMin < lecEndMin) {
                    // conflict detected
                    if (isAutoUpdating) {
                        _uiState.update {
                            it.copy(
                                isStartTimeError = false,
                                errorMessage = null,
                                conflictLecId = null,
                                startTimeMinutes = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                errorMessage = "Start time conflicts with ${lec.title}",
                                conflictLecId = lec.id,
                                isStartTimeError = true,
                                startTimeMinutes = startMin
                            )
                        }
                    }
                    return@launch
                }
            }

            // No conflict - update start time
            _uiState.update {
                it.copy(
                    errorMessage = null,
                    startTimeMinutes = startMin,
                    conflictLecId = null,
                    isStartTimeError = false
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
                        isEndTimeError = true,
                        errorMessage = "End time must be after start time",
                        endTimeMinutes = endMin
                    )
                }
                return
            }

            // Check for overlaps with existing lectures
            uiState.value.lecturesForDay.forEach { lec ->
                val lecStartMin = lec.startTimeMinute
                val lecEndMin = lec.endTimeMinute

                // Two intervals [startMin, endMin] and [lecStart, lecEnd] overlap if:
                // startMin < lecEnd AND endMin > lecStart
                val hasOverlap = startMin < lecEndMin && endMin > lecStartMin

                if (hasOverlap) {
                    // conflict detected
                    if (isAutoUpdating) {
                        _uiState.update {
                            it.copy(
                                isEndTimeError = false,
                                errorMessage = null,
                                conflictLecId = null,
                                endTimeMinutes = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isEndTimeError = true,
                                errorMessage = "Time overlaps with ${lec.title}",
                                conflictLecId = lec.id,
                                endTimeMinutes = endMin
                            )
                        }
                    }
                    return
                }
            }

            // No conflict - update end time
            _uiState.update {
                it.copy(
                    isEndTimeError = false,
                    errorMessage = null,
                    conflictLecId = null,
                    endTimeMinutes = endMin
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
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
            _uiState.update { it.copy(isLoading = true) }

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
                            isLoading = false,
                            selectedLocationId = newLocationId,
                            successMessage = "Location added successfully"
                        )
                    }
                } ?: run {
                    throw Exception("Location coordinates not available")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to add location: ${e.message}"
                    )
                }
            }
        }
    }


    fun saveLectureSlot() {
        /*viewModelScope.launch {
            val state = _uiState.value

            if (state.selectedSubjectId == null) {
                _uiState.update { it.copy(errorMessage = "Please select a subject") }
                return@launch
            }

            if (state.selectedLocationId == null) {
                _uiState.update { it.copy(errorMessage = "Please select a location") }
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
                    locationId = state.selectedLocationId,
                    dayOfWeek = state.selectedDayOfWeek.ordinal + 1,
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
                        selectedLocationId = null,
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
        }*/
    }

    fun loadLecturesForSelectedDay(dayIndex: DayOfWeek) {
        // todo: avoid execution if already selected
        viewModelScope.launch {
            _uiState.value = UpdateScheduleUiState(
                selectedDayOfWeek = dayIndex,
                subjects = uiState.value.subjects,
                selectedSubjectId = uiState.value.selectedSubjectId,
                isLoading = false,
            )
            _uiState.value.selectedDayOfWeek?.let { dayOfWeek ->
                scheduleRepository.observeLecturesWithSubjectForDay(
                    dayOfWeek
                ).collect { lecturesWithSubject ->
                    _uiState.update { state ->
                        state.copy(
                            lecturesForDay = lecturesWithSubject.map {
                                it.toDomain()
                            }
                    }.sortedBy {
                                it.startTimeMinute
                            }
                        )
                    }
                }
            }
        }

        fun deleteLectureSlot(lecture: LectureItem) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                try {
//                scheduleRepository.deleteLectureSlot(lecture.lecture)

                    _uiState.update { currUiState ->
                        currUiState.copy(
                            isLoading = false,
                            successMessage = "Lecture deleted successfully",
                            lecturesForDay = currUiState.lecturesForDay.filter { it != lecture }
                        )
                    }
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

        fun clearMessages() {
            _uiState.update {
                it.copy(
                    errorMessage = null,
                    successMessage = null
                )
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

    }
