package com.reyaz.feature.attendance.presentation.add_schedule.presentation

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.components.AddSubjectDialog
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.components.AutomationToggleNew
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.components.DaySelector
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.components.SubjectDropdownNew
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.components.TimeSelector
import com.reyaz.feature.attendance.utils.getDayName
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScheduleScreenNew(
    viewModel: UpdateScheduleViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var subjectExpanded by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf<TimePickerType?>(null) }
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Box{
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
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    SubjectDropdownNew(
                        selectedSubject = uiState.selectedSubject?.name ?: "Select Subject",
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

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TimeSelector(
                            label = "Start Time",
                            timeMinutes = uiState.startTimeMinutes,
                            onTimeClick = { showTimePicker = TimePickerType.START_TIME },
                            modifier = Modifier.weight(1f)
                        )
                        TimeSelector(
                            label = "End Time",
                            timeMinutes = uiState.endTimeMinutes,
                            onTimeClick = { showTimePicker = TimePickerType.END_TIME },
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
                    AutomationToggleNew(
                        enabled = uiState.automationEnabled,
                        onToggle = { viewModel.onAutomationToggled(it) })
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    LocationField(
//                        locationName = "Faculty of Engineering & Technology",
                        locationName = uiState.locationName,
                        onLocationClick = {
                            scope.launch {
                                viewModel.getCurrentLocation()
                            }
                        }
                    )
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
                    items= uiState.lecturesForDay
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
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                    ,
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
    }

// Time Picker Dialog
    showTimePicker?.let { type ->
        SimpleTimePickerDialog(
            initialMinutes = when (type) {
                TimePickerType.START_TIME -> uiState.startTimeMinutes
                TimePickerType.END_TIME -> uiState.endTimeMinutes
            }, onTimeSelected = { minutes ->
                when (type) {
                    TimePickerType.START_TIME -> viewModel.onStartTimeChanged(minutes)
                    TimePickerType.END_TIME -> viewModel.onEndTimeChanged(minutes)
                }
                showTimePicker = null
            }, onDismiss = { showTimePicker = null })
    }

// Add Subject Dialog
    if (showAddSubjectDialog) {
        AddSubjectDialog(onDismiss = { showAddSubjectDialog = false }, onConfirm = { subjectName ->
            viewModel.addNewSubject(subjectName)
            showAddSubjectDialog = false
        })
    }
}

