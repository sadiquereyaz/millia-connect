package com.reyaz.milliaconnect1.navigation.graph

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.reyaz.feature.attendance.presentation.add_schedule.UpdateScheduleScreen
import com.reyaz.feature.attendance.presentation.graph.components.DonutChart
import com.reyaz.feature.attendance.presentation.graph.components.MultiLineChart
import com.reyaz.feature.attendance.presentation.graph.model.dummyGraphData1
import com.reyaz.feature.attendance.presentation.graph.model.sampleDonutChartData
import com.reyaz.feature.attendance.presentation.map.MapScreen
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
        /*ScheduleScreen(
            navigateToAddSchedule = {
                navController.navigate(NavigationRoute.AddSchedule.route)
            }
        )*/
        LazyColumn() {
            item {
                MultiLineChart(
                    graphData = dummyGraphData1
                )
            }

            item {
                DonutChart(
                    data = sampleDonutChartData
                )
            }
        }
    }

    // Add/Update Schedule Screen
    composable(route = NavigationRoute.AddSchedule.route) {
        UpdateScheduleScreen()
    }

    // Mappls Map Screen
    composable(route = NavigationRoute.MapplsRoute.route) {
        MapScreen(
            onConfirm = { lat, lng ->
                Timber.d("lat: $lat, lng: $lng")
                navController.previousBackStackEntry?.savedStateHandle?.set("lat", lat)
            }
        )
    }
}


