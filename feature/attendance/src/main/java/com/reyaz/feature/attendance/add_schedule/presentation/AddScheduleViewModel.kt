package com.reyaz.feature.attendance.add_schedule.presentation

import androidx.lifecycle.ViewModel
import com.reyaz.core.location.api.LocationProvider
import com.reyaz.core.location.model.MyLocationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddScheduleViewModel(
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddScheduleUiState())
    val uiState: StateFlow<AddScheduleUiState> = _uiState.asStateFlow()

    suspend fun getCurrentLocation() {
        when(val currentLocation = locationProvider.getCurrentLocation()){
            is MyLocationResult.Success -> {
                _uiState.value = _uiState.value.copy(currentLocation = currentLocation, message = "Location Success: ${currentLocation.latitude}, ${currentLocation.longitude}, ${currentLocation.accuracy}")
            }
            is MyLocationResult.Error -> {
                _uiState.value = _uiState.value.copy(message = currentLocation.reason)
            }
            MyLocationResult.LocationDisabled -> {
                _uiState.value = _uiState.value.copy(message = "Location Disabled")
            }
            MyLocationResult.PermissionDenied -> {
                _uiState.value = _uiState.value.copy(message = "Permission Denied")
            }
            MyLocationResult.Timeout -> {
                _uiState.value = _uiState.value.copy(message = "Timeout")
            }
        }
    }

}