package com.reyaz.feature.attendance.presentation.schedule

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn


data class ScheduleUiState(
    val selectedDate: LocalDate? = null,
    val overAllPer: Int? = null,
    val targetPer: Int? = null,
){
    val todayDate = Clock.System.todayIn(timeZone = TimeZone.currentSystemDefault())
}
