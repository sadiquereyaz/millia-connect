package com.reyaz.feature.attendance.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.domain.repo.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import timber.log.Timber

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    companion object {
        const val TOTAL_ITEMS = 200
        const val CENTER_INDEX = TOTAL_ITEMS / 2
    }

    init {
        _uiState.update {
            it.copy(
                targetPer = 75,
                overAllPer = 91
            )
        }
        // Load lectures for today by default
        loadLecturesForDate(_uiState.value.selectedDate)
    }

    fun onDateSelected(selectedDate: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedDate = selectedDate
                )
            }
            loadLecturesForDate(selectedDate)
        }
    }

    // todo: when this method is called again after init, does the previous subscription get cancelled? or there is the possibility of memory leak
    private fun loadLecturesForDate(date: LocalDate) {
        scheduleRepository.observeLecturesForDate(date)
            .onEach { lectures ->
                _uiState.update {
                    it.copy(lectures = lectures)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAttendanceSelected(attendanceId: Long?, lectureId: Long, status: AttendanceStatus) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                _uiState.update {
                    it.copy(
                        isLoading = true
                    )
                }

                scheduleRepository.upsertLectureSlotAttendanceForDate(
                    attendanceId = attendanceId,
                    lectureId = lectureId,
                    date = uiState.value.selectedDate.toEpochDays(),
                    status = status
                )

                _uiState.update {
                    it.copy(
                        lectures = it.lectures.map { lectureItem ->
                            val id = lectureItem.lecture.lectureId
                            if (id == lectureId) {
                                lectureItem.attendance?.status = status
                            } else
                                lectureItem
                            return@map lectureItem
                        }
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}