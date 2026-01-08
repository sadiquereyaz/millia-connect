package com.reyaz.feature.attendance.presentation.add_schedule

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.geometry.LatLngBounds
import com.mappls.sdk.plugins.places.placepicker.PlacePicker
import com.mappls.sdk.plugins.places.placepicker.model.PlacePickerOptions
import com.reyaz.feature.attendance.domain.model.AddFieldDialogType
import com.reyaz.feature.attendance.presentation.add_schedule.components.AddTextFieildDialog
import com.reyaz.feature.attendance.presentation.add_schedule.components.AutomationSegmentButton
import com.reyaz.feature.attendance.presentation.add_schedule.components.CustomTimePicker
import com.reyaz.feature.attendance.presentation.add_schedule.components.DaySelector
import com.reyaz.feature.attendance.presentation.add_schedule.components.SimpleTimePickerDialog
import com.reyaz.feature.attendance.presentation.add_schedule.components.SubjectDropdownNew
import com.reyaz.feature.attendance.presentation.add_schedule.components.TimeSelector
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.LectureCard
import com.reyaz.feature.attendance.presentation.add_schedule.components.LocationField
import com.reyaz.feature.attendance.utils.getDayName
import com.reyaz.feature.attendance.utils.time.minutesToAmPmString
import com.reyaz.feature.attendance.utils.toDetailedString
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScheduleScreen(
    viewModel: UpdateScheduleViewModel = koinViewModel(),
    navigateToMapView: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var subjectExpanded by remember { mutableStateOf(false) }
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Show snackbar for messages
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
        if (uiState.successMessage != null || uiState.errorMessage != null) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearMessages()
        }
    }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var showAddLocationNameDialog by remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(result.data!!)
                // update location coordinates in viewmodel/uiState and save in location table
                Timber.d(place?.toDetailedString() ?: "Place is null")
                showAddLocationNameDialog = if (place?.poi.isNullOrBlank()) {
                    // ask for place name and give suggestion SubjectName classroom building
                    true
                } else {
                    false
                    // placeName = place.formattedAddress ?: place.poi
                }
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
                DaySelector(
                    selectedDay = uiState.selectedDayOfWeek,
                    onDaySelected = { viewModel.onDaySelected(it) }
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                SubjectDropdownNew(
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
                /*Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // start time
                    CustomTimePicker(
                        modifier = Modifier.weight(1f),
                        value = minutesToAmPmString(uiState.startTimeMinutes),
                        label = "Start Time",
                        onTimeSelect = { localTime ->
                            //viewModel.setStartTime(localTime)
                            //viewModel.setEndTime(localTime.plusHours(1))
                        },
                        isEnabled = true
                    )
                    // end time
                    CustomTimePicker(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        value = minutesToAmPmString(uiState.endTimeMinutes),
                        label = "End Time",
                        onTimeSelect = { localTime ->
                            //viewModel.setEndTime(localTime)
                        },
                        isEnabled = true
                    )
                }*/
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
                            viewModel.updateStartTime(it)
                            viewModel.updateEndTime(it+60)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    TimeSelector(
                        label = "End Time",
                        timeMinutes = uiState.endTimeMinutes,
                        onTimeClick = {
                            viewModel.updateEndTime(it)
                        },
                        modifier = Modifier.weight(1f)
                    )

                }
            }

            item {
                Spacer(Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))
            }

            item {
                AutomationSegmentButton(
                    onSelected = { viewModel.onAutomationSelected(it) },
                    selectedIndex = uiState.automationSegSelectedIndex
                )
                Spacer(Modifier.height(16.dp))
            }

            item {
                AnimatedVisibility(
                    visible = uiState.automationSegSelectedIndex == 0,
                    enter = slideInVertically(
                        initialOffsetY = { it } // starts below and slides down into place
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { it } // slides down and exits below
                    ) + fadeOut()
                ) {
                    LocationField(
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

                            launcher.launch(intent)
                        }
                    )
                }
            }

            if (uiState.lecturesForDay.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Lectures on ${getDayName(uiState.selectedDayOfWeek)}:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
            // lecture list
            items(
                key = { it.lecture.lectureId },
                items = uiState.lecturesForDay
            ) { lecture ->
                LectureCard(
                    lecture = lecture,
                    onDelete = { viewModel.deleteLectureSlot(lecture) }
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
                enabled = !uiState.isLoading
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
    }

    // Add Subject Dialog
    if (showAddSubjectDialog) {
        AddTextFieildDialog(
            onDismiss = { showAddSubjectDialog = false },
            onConfirm = { subjectName ->
                viewModel.addNewSubject(subjectName)
                showAddSubjectDialog = false
            },
            type = AddFieldDialogType.SUBJECT
        )
    }
    // Add Location Dialog
    if (showAddLocationNameDialog) {
        AddTextFieildDialog(
            onDismiss = { showAddLocationNameDialog = false },
            type = AddFieldDialogType.LOCATION,
            onConfirm = { locationName ->
                viewModel.onLocationNameChanged(locationName)
                showAddLocationNameDialog = false
            }
        )
    }
}

