package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.reyaz.feature.attendance.add_schedule.presentation.AddAttendanceScreen
import com.reyaz.feature.attendance.add_schedule.presentation.AddScheduleViewModel
import constants.NavigationRoute
import com.reyaz.feature.attendance.schedule.presentation.ScheduleScreen
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Coming Soon...")
        }
    }
}


