package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.reyaz.feature.attendance.add_schedule.presentation.AddAttendanceScreen
import com.reyaz.feature.attendance.add_schedule.presentation.AddScheduleViewModel
import com.reyaz.feature.attendance.presentation.schedule.ScheduleScreen
import com.reyaz.feature.attendance.schedule.presentation.ScheduleScreenOld
import org.koin.androidx.compose.koinViewModel

/**
 * Attendance feature navigation graph
 */
internal fun NavGraphBuilder.attendanceNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {

    // Schedule Screen
    composable(route = constants.NavigationRoute.AddSchedule.route) {
        val viewModel: AddScheduleViewModel = koinViewModel()
        AddAttendanceScreen(
            viewModel = viewModel
        )
    }

    // Schedule Screen
    composable(route = constants.NavigationRoute.Schedule.route) {
        ScheduleScreen()
    }
}


