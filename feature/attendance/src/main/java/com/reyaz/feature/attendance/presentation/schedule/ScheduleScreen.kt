package com.reyaz.feature.attendance.presentation.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.attendance.presentation.schedule.components.HorizontalCalendar
import com.reyaz.feature.attendance.presentation.schedule.components.LectureList
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = koinViewModel(),
    navigateToAddSchedule: () -> Unit

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column {
        HorizontalCalendar(
            selectedDate = uiState.selectedDate ?: uiState.todayDate,
            onDateSelected = {
                viewModel.onDateSelected(it)
            },
            totalAttendancePer = uiState.overAllPer,
            targetPer = uiState.targetPer,
            todayDate = uiState.todayDate
        )
        LectureList(
            lectures = uiState.lectures,
            onAttendanceTypeSelected = { lectureId, presentType ->
                // TODO: Handle attendance type selection
            },
            onAddSchedule = navigateToAddSchedule
        )
    }
}
