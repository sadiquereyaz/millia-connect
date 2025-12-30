package com.reyaz.feature.attendance.presentation.add_schedule.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.attendance.data.local.model.LectureWithSubject
import kotlinx.datetime.DayOfWeek
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.launch

enum class TimePickerType {
    START_TIME, END_TIME
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScheduleScreenNew(
    viewModel: UpdateScheduleViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var subjectExpanded by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf<TimePickerType?>(null) }
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
        topBar = {
            TopAppBar(
                title = { Text("Update Schedule", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                DaySelectorNew(
                    selectedDay = uiState.selectedDayOfWeek,
                    onDaySelected = { viewModel.onDaySelected(it) }
                )
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
                    }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TimeSelectorNew(
                        label = "Start Time",
                        timeMinutes = uiState.startTimeMinutes,
                        onTimeClick = { showTimePicker = TimePickerType.START_TIME },
                        modifier = Modifier.weight(1f)
                    )
                    TimeSelectorNew(
                        label = "End Time",
                        timeMinutes = uiState.endTimeMinutes,
                        onTimeClick = { showTimePicker = TimePickerType.END_TIME },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                AutomationToggleNew(
                    enabled = uiState.automationEnabled,
                    onToggle = { viewModel.onAutomationToggled(it) }
                )
            }

            item {
                LocationFieldNew(
                    location = uiState.locationName.ifBlank { "e.g. Faculty of Engg. JMI" },
                    onLocationClick = {
                        scope.launch {
                            viewModel.getCurrentLocation()
                        }
                    }
                )
            }

            item {
                Text(
                    "Lectures on ${getDayName(uiState.selectedDayOfWeek)}:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(uiState.lecturesForDay) { lecture ->
                LectureCardNew(
                    lecture = lecture,
                    onDelete = { viewModel.deleteLectureSlot(lecture) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.saveLectureSlot() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
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
            },
            onTimeSelected = { minutes ->
                when (type) {
                    TimePickerType.START_TIME -> viewModel.onStartTimeChanged(minutes)
                    TimePickerType.END_TIME -> viewModel.onEndTimeChanged(minutes)
                }
                showTimePicker = null
            },
            onDismiss = { showTimePicker = null }
        )
    }
}

@Composable
fun DaySelectorNew(
    selectedDay: DayOfWeek,
    onDaySelected: (DayOfWeek) -> Unit
) {
    val days = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        days.forEach { day ->
            DayButtonNew(
                day = getDayAbbreviation(day),
                isSelected = day == selectedDay,
                onClick = { onDaySelected(day) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DayButtonNew(
    day: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDropdownNew(
    selectedSubject: String,
    subjects: List<com.reyaz.feature.attendance.data.local.model.SubjectEntity>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSubjectSelected: (Long) -> Unit
) {
    Column {
        Text(
            "Select Subject",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            OutlinedTextField(
                value = selectedSubject,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
                colors = OutlinedTextFieldDefaults.colors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                subjects.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(subject.name) },
                        onClick = { onSubjectSelected(subject.subjectId) }
                    )
                }
            }
        }
    }
}

@Composable
fun TimeSelectorNew(
    label: String,
    timeMinutes: Int,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = formatMinutesToTime(timeMinutes),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Pick time")
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onTimeClick)
        )
    }
}

@Composable
fun AutomationToggleNew(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Automation:", fontWeight = FontWeight.Medium)
            Row(
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp))
                    .height(40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = if (enabled) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)
                        )
                        .clickable { onToggle(true) }
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Yes",
                        color = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            color = if (!enabled) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
                        )
                        .clickable { onToggle(false) }
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No",
                        color = if (!enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        Text(
            "Your attendance will be marked automatically based on the location of the device.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun LocationFieldNew(
    location: String,
    onLocationClick: () -> Unit
) {
    Column {
        Text(
            "Class Location",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = location,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = onLocationClick) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Pick location")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onLocationClick)
        )
        Text(
            "Use current location for more precise results.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun LectureCardNew(
    lecture: LectureWithSubject,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    lecture.subject.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "${formatMinutesToTime(lecture.lecture.startTimeMinutes)} - ${formatMinutesToTime(lecture.lecture.endTimeMinutes)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Row {
                IconButton(onClick = { }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleTimePickerDialog(
    initialMinutes: Int,
    onTimeSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(initialMinutes / 60) }
    var minute by remember { mutableStateOf(initialMinutes % 60) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hour selector
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { hour = (hour + 1) % 24 }) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase hour")
                    }
                    Text(
                        text = String.format("%02d", hour),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { hour = if (hour == 0) 23 else hour - 1 }) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease hour")
                    }
                }

                Text(
                    text = ":",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Minute selector
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { minute = (minute + 5) % 60 }) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase minute")
                    }
                    Text(
                        text = String.format("%02d", minute),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { minute = if (minute < 5) 55 else minute - 5 }) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease minute")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(hour * 60 + minute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Helper functions
fun formatMinutesToTime(minutes: Int): String {
    val hour = minutes / 60
    val minute = minutes % 60
    val period = if (hour < 12) "AM" else "PM"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return String.format("%d:%02d %s", displayHour, minute, period)
}

fun getDayAbbreviation(day: DayOfWeek): String {
    return when (day) {
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        DayOfWeek.SUNDAY -> "Sun"
    }
}

fun getDayName(day: DayOfWeek): String {
    return when (day) {
        DayOfWeek.MONDAY -> "Monday"
        DayOfWeek.TUESDAY -> "Tuesday"
        DayOfWeek.WEDNESDAY -> "Wednesday"
        DayOfWeek.THURSDAY -> "Thursday"
        DayOfWeek.FRIDAY -> "Friday"
        DayOfWeek.SATURDAY -> "Saturday"
        DayOfWeek.SUNDAY -> "Sunday"
    }
}