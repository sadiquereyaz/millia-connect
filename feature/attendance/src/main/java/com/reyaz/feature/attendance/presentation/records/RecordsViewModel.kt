package com.reyaz.feature.attendance.presentation.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.feature.attendance.domain.usecase.GetAttendanceGraphUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecordsViewModel(
    private val getAttendanceGraphUseCase: GetAttendanceGraphUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchRecords()
    }

    private fun fetchRecords() {
        _uiState.update {
            it.copy(
                isLoading = false,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val records = getAttendanceGraphUseCase.invoke()
            _uiState.update {
                it.copy(
                    multiLineData = records.first,
                    donutChartData = records.second,
                    isLoading = false,
                )
            }
        }
    }

}