package com.reyaz.feature.attendance.add_schedule.presentation

import com.reyaz.core.location.model.MyLocationResult

data class AddScheduleUiState(
    val currentLocation: MyLocationResult? = null,
    val message: String? = null
)