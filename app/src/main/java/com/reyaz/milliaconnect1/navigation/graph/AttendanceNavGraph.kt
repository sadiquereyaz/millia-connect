package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.reyaz.feature.attendance.presentation.add_schedule.UpdateScheduleScreen
import com.reyaz.feature.attendance.presentation.map.MapPickerScreen
import com.reyaz.feature.attendance.presentation.schedule.ScheduleScreen
import constants.NavigationRoute
import timber.log.Timber

/**
 * Attendance feature navigation graph
 */
internal fun NavGraphBuilder.attendanceNavGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {

    // Schedule Screen
    composable(route = NavigationRoute.Schedule.route) {
        ScheduleScreen(
            navigateToAddSchedule = {
                navController.navigate(NavigationRoute.AddSchedule.route)
            }
        )
    }

    // Add/Update Schedule Screen
    composable(route = NavigationRoute.AddSchedule.route) {
        UpdateScheduleScreen(
            navigateToMapView = {
                navController.navigate(NavigationRoute.MapplsRoute.route)
            }
        )
    }

    // Mappls Map Screen
    composable(route = NavigationRoute.MapplsRoute.route) {
        MapPickerScreen(
            onConfirm = { lat, lng ->
                Timber.d("lat: $lat, lng: $lng")
                 navController.previousBackStackEntry?.savedStateHandle?.set("lat", lat)
            }
        )
    }
}


