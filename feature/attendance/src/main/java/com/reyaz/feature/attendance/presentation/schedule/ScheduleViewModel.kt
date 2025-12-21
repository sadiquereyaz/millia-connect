package com.reyaz.feature.attendance.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class ScheduleViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                targetPer = 75,
                overAllPer = 91
            )
        }
    }
    fun onDateSelected(selectedDate: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedDate = selectedDate
                )
            }
        }
    }

    companion object {
         const val TOTAL_ITEMS = 200
         const val CENTER_INDEX = TOTAL_ITEMS / 2
    }
}