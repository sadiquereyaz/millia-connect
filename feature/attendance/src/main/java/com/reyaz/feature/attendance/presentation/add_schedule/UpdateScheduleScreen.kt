package com.reyaz.feature.attendance.presentation.add_schedule

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.geometry.LatLngBounds
import com.mappls.sdk.plugins.places.placepicker.PlacePicker
import com.mappls.sdk.plugins.places.placepicker.model.PlacePickerOptions
import com.reyaz.core.common.utils.extensions.StringUtils.capitalizeWordLevel
import com.reyaz.feature.attendance.domain.model.AddFieldDialogType
import com.reyaz.feature.attendance.domain.model.dummyLectures
import com.reyaz.feature.attendance.domain.model.dummyLocations
import com.reyaz.feature.attendance.presentation.add_schedule.components.AddTextFieldDialog
import com.reyaz.feature.attendance.presentation.add_schedule.components.AutomationSegmentButton
import com.reyaz.feature.attendance.presentation.add_schedule.components.DaySelector
import com.reyaz.feature.attendance.presentation.add_schedule.components.LocationComponents
import com.reyaz.feature.attendance.presentation.add_schedule.components.SubjectDropdown
import com.reyaz.feature.attendance.presentation.add_schedule.components.TimeSelector
import com.reyaz.feature.attendance.presentation.add_schedule.components.UpdateScreenLectureCard
import com.reyaz.feature.attendance.utils.TimeUtils.getDayName
import com.reyaz.feature.attendance.utils.toDetailedString
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScheduleScreen(
    viewModel: UpdateScheduleViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var subjectExpanded by remember { mutableStateOf(false) }
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var showAddLocationNameDialog by remember { mutableStateOf(false) }

    // Permission launcher for location
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            Timber.d("Location permission granted")
            // Permission granted, location-based automation can work
            viewModel.onUpdateLocationPermission(true)
        } else {
            Timber.d("Location permission denied")
            // Show message to user that automation won't work without permission
            viewModel.onUpdateLocationPermission(false)
        }
    }

    val placePickerLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(result.data!!)
                    viewModel.onLocationPicked(place?.lat, place?.lng)
                // update location coordinates in viewmodel/uiState and save in location table
//                Timber.d(place?.toDetailedString() ?: "Place is null")
                showAddLocationNameDialog = if (place?.poi.isNullOrBlank()) {
                    true
                } else {
                    place.poi?.let { viewModel.addNewLocation(it) }
                    false
                    // placeName = place.formattedAddress ?: place.poi
                }
            }
        }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.automationSegSelectedIndex) {
        if (uiState.automationSegSelectedIndex == 0) {
            // Check if location permissions are already granted
            val fineLocationGranted = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val coarseLocationGranted = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!fineLocationGranted && !coarseLocationGranted) {
                // Request location permissions
                locationPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                Timber.d("Location permissions already granted")
            }
        }
    }

    val listState = rememberLazyListState()
    val header = 5

    LaunchedEffect(uiState.conflictLecId) {
        val index = uiState.lecturesForDay
            .indexOfFirst { it.id == uiState.conflictLecId }

        if (index != -1) {
            listState.animateScrollToItem(header + index)
        }
    }

    Box(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            focusManager.clearFocus()
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            // days
            item {
                uiState.selectedDayOfWeek?.let {
                    DaySelector(
                        selectedDay = it,
                        onDaySelected = { viewModel.loadLecturesForSelectedDay(it) }
                    )
                }
                Spacer(Modifier.height(20.dp))
            }

            item {
                SubjectDropdown(
                    selectedSubject = uiState.selectedSubject?.name ?: "",
                    subjects = uiState.subjects,
                    expanded = subjectExpanded,
                    onExpandedChange = { subjectExpanded = it },
                    onSubjectSelected = {
                        viewModel.onSubjectSelected(it)
                        subjectExpanded = false
                    },
                    onAddNewSubject = {
                        subjectExpanded = false
                        showAddSubjectDialog = true
                    },
                    imeAction = ImeAction.Next,
                )
                Spacer(Modifier.height(16.dp))
            }

            // times
            item {
                if (uiState.selectedSubject != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TimeSelector(
                            label = "Start Time",
                            timeMinutes = uiState.startTimeMinutes,
                            onTimeClick = {
                                viewModel.onStartTimeChanged(it)
                            },
                            modifier = Modifier.weight(1f),
                            isClickable = uiState.selectedSubject != null,
                            isError = uiState.isStartTimeError
                        )
                        if (uiState.isEndTimeFieldVisible)
                            TimeSelector(
                                label = "End Time",
                                timeMinutes = uiState.endTimeMinutes,
                                onTimeClick = {
                                    viewModel.onEndTimeChanged(it)
                                },
                                modifier = Modifier.weight(1f),
                                isClickable = uiState.startTimeMinutes != null,
                                isError = uiState.isEndTimeError
                            )
                    }
                }
            }

            // automation switch
            item {
                if (uiState.endTimeMinutes != null && uiState.conflictLecId == null) {

                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))

                    AutomationSegmentButton(
                        onSelected = { viewModel.onAutomationSelected(it) },
                        selectedIndex = uiState.automationSegSelectedIndex
                    )
                    AnimatedVisibility(
                        visible = uiState.automationSegSelectedIndex == 0,
                        enter = slideInVertically(
                            initialOffsetY = { it } // starts below and slides down into place
                        ) + fadeIn(),
                        exit = slideOutVertically(
                            targetOffsetY = { it } // slides down and exits below
                        ) + fadeOut()
                    ) {
                        Spacer(Modifier.height(16.dp))

                        LocationComponents(
                            locationList = uiState.locationList,
                            selectedId = uiState.selectedLocationId,
                            onLocationSelect = {
                                viewModel.onLocationSelected(it)
                            },
                            navigateToPicker = {
                                val jmiCampusBoundary = LatLngBounds.Builder()
                                    .include(LatLng(28.54747029107844, 77.27730520782745))
                                    .include(LatLng(28.571173717168453, 77.29304218757096))
                                    .build()
                                val intent = PlacePicker.IntentBuilder()
                                    .placeOptions(
                                        PlacePickerOptions.builder()
                                            .startingBounds(jmiCampusBoundary)
                                            .includeSearch(true)
                                            .includeDeviceLocationButton(true)
                                            .build()
                                    )
                                    .build(context as Activity?)

                                placePickerLauncher.launch(intent)
                            }
                        )
                    }
                }
            }

            // lecture list divider and heading
            item {
                if (uiState.lecturesForDay.isNotEmpty()) {
                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Lectures on ${getDayName(uiState.selectedDayOfWeek)}:",
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            // lecture list
            items(
                key = { it.id },
                items = uiState.lecturesForDay
            ) { lecture ->
                UpdateScreenLectureCard(
                    lecture = lecture,
                    onDelete = {
                        //viewModel.deleteLectureSlot(lecture)
                    },
                    isInConflict = lecture.id == uiState.conflictLecId
                )
                Spacer(Modifier.height(16.dp))
            }

            item {
                Spacer(
                    Modifier
                        .height(ButtonDefaults.MinHeight + 32.dp)
                        .background(Color.Red)
                )
            }
        }

        // save button
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = { viewModel.saveLectureSlot() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = uiState.isSaveEnabled
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), color = Color.White
                    )
                } else {
                    Text("Save", fontSize = 18.sp)
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Add Subject Dialog
    if (showAddSubjectDialog) {
        AddTextFieldDialog(
            onDismiss = { showAddSubjectDialog = false },
            onConfirm = { subjectName ->
                viewModel.addNewSubject(subjectName)
            },
            type = AddFieldDialogType.SUBJECT,
            suggestOptions = uiState.subjects.map { it.name }
        )
    }
    // Add Location Dialog
    if (showAddLocationNameDialog) {
        AddTextFieldDialog(
            onDismiss = { showAddLocationNameDialog = false },
            type = AddFieldDialogType.LOCATION,
            onConfirm = { locationName ->
                viewModel.addNewLocation(locationName.capitalizeWordLevel())
            },
            suggestOptions = uiState.locationList.map { it.name }
        )
    }
}