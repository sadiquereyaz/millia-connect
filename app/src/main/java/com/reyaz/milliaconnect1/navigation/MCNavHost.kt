package com.reyaz.milliaconnect1.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.reyaz.core.common.utils.openUrl
import com.reyaz.core.config.AppViewModel
import com.reyaz.core.config.ForceUpdateDialog
import com.reyaz.core.config.UpdateState
import com.reyaz.core.ui.screen.PdfViewerScreen
import com.reyaz.feature.notice.presentation.NoticeScreen
import com.reyaz.feature.notice.presentation.NoticeViewModel
import com.reyaz.feature.portal.presentation.PortalScreen
import com.reyaz.feature.portal.presentation.PortalViewModel
import com.reyaz.milliaconnect1.navigation.graph.attendanceNavGraph
import com.reyaz.milliaconnect1.navigation.graph.propertyNavGraph
import com.reyaz.milliaconnect1.navigation.graph.resultNavGraph
import constants.NavigationRoute
import org.koin.androidx.compose.koinViewModel

/**
 * Main navigation host for the Millia Connect App
 * Manages all navigation graphs and routing logic
 */
@Composable
fun MCNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    showSearchComponents: Boolean
) {
    val appViewModel = koinViewModel<AppViewModel>()
    val updateState by appViewModel.updateState.collectAsState()

    Box {

        if (updateState is UpdateState.Force)
            ForceUpdateDialog(message = (updateState as UpdateState.Force).message)

        NavHost(
            navController = navController,
            startDestination =
//            NavigationRoute.AttendanceGraph.route,
                NavigationRoute.Portal.route,
//            constants.NavigationRoute.ResultGraph.route,
//            constants.NavigationRoute.PropertyGraph.route,
//        NavigationRoute.Notice.route,
            modifier = modifier.fillMaxSize()
        ) {
            // Attendance Feature Graph
            navigation(
                route = constants.NavigationRoute.AttendanceGraph.route,
                startDestination = constants.NavigationRoute.Schedule.route
            ) {
                attendanceNavGraph(navController, snackbarHostState)
            }

            composable(route = constants.NavigationRoute.Portal.route) {
                val portalViewModel: PortalViewModel = koinViewModel()
                PortalScreen(
                    viewModel = portalViewModel,
                    navController = navController,
                    dismissDialog = {
                        navController.navigateUp()
                    }
                )
            }
            // pdf screen
            composable(
                route = constants.NavigationRoute.PdfViewer.route,
                arguments = listOf(navArgument("path") { type = NavType.StringType })
            ) { backStackEntry ->
                val path = backStackEntry.arguments?.getString("path") ?: ""
                if (path.isNotEmpty())
                    PdfViewerScreen(filePath = path)
            }

            // Result Graph
            navigation(
                route = constants.NavigationRoute.ResultGraph.route,
                startDestination = constants.NavigationRoute.Result.route
            ) {
                resultNavGraph(navController, snackbarHostState)
            }

            // Property Graph
            /*navigation(
                route = constants.NavigationRoute.PropertyGraph.route,
                startDestination = constants.NavigationRoute.PropertyFeed.route//changes mad by me
            ) {
                propertyNavGraph(navController, snackbarHostState, showSearchComponents)
            }*/

            // Notice
            composable(
                route = constants.NavigationRoute.NoticeGraph.route
            ) {
                val noticeViewModel: NoticeViewModel = koinViewModel()
                val uiState by noticeViewModel.uiState.collectAsStateWithLifecycle()
                NoticeScreen(
                    uiState = uiState,
                    onEvent = noticeViewModel::event,
                    openPdf = {
                        navController.navigate(constants.NavigationRoute.PdfViewer.createRoute(it))
                    },
                    modifier = Modifier,
                    snackbarHostState = snackbarHostState
                )
            }

            // Side Navigation Graph
            /*navigation(
                route = NavigationRoute.AcademicGraph.route,
                startDestination = NavigationRoute.Academics.route,
            ) {
                //academicNavGraph(navController, snackbarHostState)
            }*/
        }
    }
}

