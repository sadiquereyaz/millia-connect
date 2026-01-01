package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.UpdateScheduleScreenNew
import com.reyaz.feature.attendance.presentation.schedule.ScheduleScreen

/**
 * Attendance feature navigation graph
 */
internal fun NavGraphBuilder.attendanceNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {

    // Schedule Screen
    composable(route = constants.NavigationRoute.Schedule.route) {
        ScheduleScreen(
            navigateToAddSchedule = {
                navController.navigate(constants.NavigationRoute.AddSchedule.route)
            }
        )
    }

    // Add/Update Schedule Screen
    composable(route = constants.NavigationRoute.AddSchedule.route) {
        UpdateScheduleScreenNew()
    }
}


