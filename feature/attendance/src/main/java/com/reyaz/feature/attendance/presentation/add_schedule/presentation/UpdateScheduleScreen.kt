package com.reyaz.feature.attendance.presentation.add_schedule.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.core.ui.components.SingleChoiceSegmentedButton
import com.reyaz.core.ui.components.SingleLineText
import com.reyaz.core.ui.components.text.DottedBorderText
import com.reyaz.core.ui.components.text.DottedUnderlineText
import com.reyaz.core.ui.components.text.dottedBorder
import com.reyaz.core.ui.components.text_field.CustomCircularTextField
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
                    DaySelectorNew(
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
                    LocationFieldNew(
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
                    LectureCardNew(
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

@Composable
fun DaySelectorNew(
    selectedDay: DayOfWeek, onDaySelected: (DayOfWeek) -> Unit
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

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemSpacing = 8.dp
    val visibleItems = 5
    val horizontalPadding = 32.dp
    val itemWidth =
        (screenWidth - horizontalPadding - (itemSpacing * (visibleItems - 1))) / visibleItems

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        items(
            items = days
        ) { day ->
            DayButtonNew(
                day = getDayAbbreviation(day),
                isSelected = day == selectedDay,
                onClick = { onDaySelected(day) },
                modifier = Modifier.width(itemWidth)
            )
        }
    }
}

@Composable
fun DayButtonNew(
    day: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
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
    onSubjectSelected: (Long) -> Unit,
    onAddNewSubject: () -> Unit,
    imeAction: ImeAction = ImeAction.Unspecified,
) {
    Column (
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ){
        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange = onExpandedChange
        ) {
            CustomCircularTextField(
                value = selectedSubject,
                onValueChange = {},
                cornerRadius = 12,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                label = "Subject",
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
                imeAction = imeAction
            )
            ExposedDropdownMenu(
                expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                subjects.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(text = subject.name, fontSize = 16.sp) },
                        onClick = { onSubjectSelected(subject.subjectId) })
                }

                if (subjects.isNotEmpty()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                "Add New Subject",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 18.sp
                            )
                        }
                    }, leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.AddCircle,
                            contentDescription = "Add new subject",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }, onClick = onAddNewSubject
                )
            }
        }
    }
}

@Composable
fun TimeSelectorNew(
    label: String, timeMinutes: Int, onTimeClick: () -> Unit, modifier: Modifier = Modifier
) {
    val cornerRadius = 12
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius.dp))
                .clickable(onClick = onTimeClick)
        ) {
            CustomCircularTextField(
                value = formatMinutesToTime(timeMinutes),
                onValueChange = {},
                label = label,
//                readOnly = true,
                enabled = false,
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Pick time")
                },
                cornerRadius = cornerRadius,
                outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AutomationToggleNew(
    enabled: Boolean, onToggle: (Boolean) -> Unit
) {
    var selectedIndex: Int? by remember { mutableStateOf(null) }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Automation:", fontWeight = FontWeight.Medium, fontSize = 20.sp)
            SingleChoiceSegmentedButton(
                options = listOf("Yes", "No"), onOptionSelect = {
                    selectedIndex = it
                }, selectedIndex = selectedIndex
            )
        }
        Text(
            "Your attendance will be marked automatically based on the location of the device.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 4.dp).padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun LocationFieldNew(
    modifier: Modifier = Modifier,
    locationName: String,
    onLocationClick: () -> Unit,
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6B73FF), Color(0xFF784BA0)
        )
    )

    if (locationName.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth().padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    // open map
                }
                .background(gradientBrush)
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "location",
                    tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    SingleLineText(
                        modifier = Modifier.padding(end = 16.dp),
                        text = "Locate Classroom",
                        maxLine = 1,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    SingleLineText(
                        text = "powered by Mappls",
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontStyle = FontStyle.Italic,

                        )
                }

                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(32.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                            shape = CircleShape
                        ),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "location",
                    tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                )
            }
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "location",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            DottedUnderlineText(
                text = locationName,
                fontSize = 18.sp,
                textColor = MaterialTheme.colorScheme.primary,
                underlineColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    // open mapplse map screen
                }
            )
        }
    }
}

@Composable
fun LectureCardNew(
    lecture: LectureWithSubject,
    onDelete: () -> Unit
) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .dottedBorder()
                .padding(horizontal = 16.dp, 8.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    lecture.subject.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp
                )
                Text(
                    "${formatMinutesToTime(lecture.lecture.startTimeMinutes)} - ${
                        formatMinutesToTime(
                            lecture.lecture.endTimeMinutes
                        )
                    }",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.Medium,
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

@Composable
fun SimpleTimePickerDialog(
    initialMinutes: Int, onTimeSelected: (Int) -> Unit, onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(initialMinutes / 60) }
    var minute by remember { mutableStateOf(initialMinutes % 60) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Select Time") }, text = {
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
                    Icon(
                        Icons.Default.KeyboardArrowDown, contentDescription = "Decrease minute"
                    )
                }
            }
        }
    }, confirmButton = {
        TextButton(onClick = {
            onTimeSelected(hour * 60 + minute)
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
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

@Composable
fun AddSubjectDialog(
    onDismiss: () -> Unit, onConfirm: (String) -> Unit
) {
    var subjectName by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = onDismiss, title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text("Add New Subject")
        }
    }, text = {
        Column {
            OutlinedTextField(
                value = subjectName,
                onValueChange = {
                    subjectName = it
                    isError = it.isBlank()
                },
                label = { Text("Subject Name") },
                placeholder = { Text("e.g. Engineering Mathematics") },
                isError = isError,
                supportingText = if (isError) {
                    { Text("Subject name cannot be empty") }
                } else null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth())
        }
    }, confirmButton = {
        Button(
            onClick = {
                if (subjectName.isNotBlank()) {
                    onConfirm(subjectName.trim())
                } else {
                    isError = true
                }
            }, enabled = subjectName.isNotBlank()
        ) {
            Text("Add")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}