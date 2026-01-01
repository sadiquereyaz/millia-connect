package com.reyaz.feature.attendance.presentation.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ModeStandby
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.core.ui.components.SingleLineText
import com.reyaz.core.ui.extensions.dottedBorder
import com.reyaz.feature.attendance.presentation.components.HorizontalCalendar
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = koinViewModel(),
    navigateToAddSchedule: () -> Unit

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column {
        HorizontalCalendar(
            selectedDate = uiState.selectedDate ?: uiState.todayDate,
            onDateSelected = {
                viewModel.onDateSelected(it)
            },
            totalAttendancePer = uiState.overAllPer,
            targetPer = uiState.targetPer,
            todayDate = uiState.todayDate
        )
        LectureList(
            lectures = uiState.lectures,
            onAttendanceTypeSelected = { lectureId, presentType ->
                // TODO: Handle attendance type selection
            },
            onAddSchedule = navigateToAddSchedule
        )
    }
}

@Composable
fun LectureList(
    lectures: List<com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject>,
    onAttendanceTypeSelected: (Long, PresentType) -> Unit,
    modifier: Modifier = Modifier,
    onAddSchedule: () -> Unit,
) {
    if (lectures.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize(0.8f)
                    .dottedBorder()
                    .clickable {
                        onAddSchedule()
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Default.Add,
                    "add schedule",
                    modifier = Modifier
                        .size(68.dp)
                        .border(1.dp, color = MaterialTheme.colorScheme.onSurface, CircleShape),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    modifier = Modifier,
                    text = "No Schedule found for this day,\nClick to add.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    } else {
        val listState = rememberLazyListState()

        // Check if the last item is visible
        val isLastItemVisible by remember {
            derivedStateOf {
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                lastVisibleItemIndex != null && lastVisibleItemIndex >= lectures.size - 1
            }
        }

        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(16.dp, 8.dp)
            ) {
                items(
                    count = lectures.size,
                    key = { index -> lectures[index].lecture.lectureId }
                ) { index ->
                    LectureItem(
                        lectureData = lectures[index],
                        onAttendanceTypeSelected = onAttendanceTypeSelected
                    )
                }
                item {
                    Spacer(Modifier.height(ButtonDefaults.MinHeight + 32.dp))
                }
            }

            // Show FAB when last item is visible
            if (isLastItemVisible) {
                SmallFloatingActionButton(
                    onClick = onAddSchedule,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(16.dp, 16.dp)
                    ) {
                        Text(text = "Edit/Modify", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Modify schedule"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LectureItem(
    lectureData: com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject,
    onAttendanceTypeSelected: (Long, PresentType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val attendancePerColor =
        if (Random.nextBoolean()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val task: String? = null
//    val locationName: String? = null // TODO: Add location support
    val locationName: String? =
        "Faculty of Engineering and Technology" // TODO: Add location support
    val taskColor = MaterialTheme.colorScheme.outline
    val warningColor =
        if (Random.nextBoolean()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val isDarkMode = isSystemInDarkTheme()

    // Convert AttendanceStatus to PresentType
    val selectedAttendanceType =
        lectureData.attendance?.status?.toPresentType() ?: PresentType.NOT_COUNTED

    // Convert minutes to time string
    val startTime = minutesToTimeString(lectureData.lecture.startTimeMinutes)
    val endTime = minutesToTimeString(lectureData.lecture.endTimeMinutes)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // its height becomes the height of the tallest child.
    ) {
        // times
        Text(
            text = "$startTime\n-\n$endTime",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            modifier = Modifier
//                .background(Color.Red)
                .width(60.dp)
        )

        // line left space
        Spacer(Modifier.width(4.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(6.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )

            // vertical line
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(0.6.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )

        }
        // line right space
        Spacer(Modifier.width(8.dp))

        // subject info box
        Box(
            Modifier.padding(
                bottom = 16.dp
            )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // attendance percent circle
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(48.dp)
                            .background(attendancePerColor.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "64%",
                            fontWeight = FontWeight.Bold,
                            color = attendancePerColor
                        )
                    }

//                        Spacer(Modifier.weight(1f))
                    Spacer(Modifier.width(4.dp))

                    Column(
                        Modifier.padding(horizontal = 8.dp)
                    ) {
                        SingleLineText(
                            text = lectureData.subject.name,
                            fontSize = 18.sp
                        )
                        // location
                        locationName?.let {
//                            Spacer(Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.size(12.dp),
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "task icon",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                )
                                Spacer(Modifier.width(4.dp))
                                SingleLineText(
                                    text = it,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                )
                            }
                        }
//                        Spacer(Modifier.height(4.dp))
                        // warning
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(12.dp),
                                imageVector = Icons.Default.ModeStandby,
                                contentDescription = "task icon",
                                tint = warningColor,
                            )
                            Spacer(Modifier.width(2.dp))
                            SingleLineText(
                                text = "You can miss this lecture",
                                color = warningColor,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    /*Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "lecture reminder"
                    )*/

                }

                Spacer(Modifier.height(12.dp))

                // task
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /*Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Task, contentDescription = "task icon",
                        tint = taskColor,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = task ?: "Add task...",
                        color = taskColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )*/
                    Spacer(Modifier.weight(1f))
                    // atttemdance types
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PresentType.entries.forEach { type ->
                            Box(
                                modifier = Modifier
                                    .clip(
                                        shape = CircleShape
//                                            shape = RoundedCornerShape(50)
                                    )
                                    .clickable {
                                        onAttendanceTypeSelected(
                                            lectureData.lecture.lectureId,
                                            type
                                        )
                                    }
                                    .border(
                                        width = 1.dp,
                                        color = type.getColor(isDarkMode),
                                        shape = CircleShape
                                    )
                                    .then(
                                        if (type == selectedAttendanceType) {
                                            Modifier
                                                .height(24.dp)
                                                .background(type.getColor(isDarkMode))
                                                .padding(horizontal = 8.dp)
                                        } else {
                                            Modifier.size(24.dp)
                                        }
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                SingleLineText(
                                    text = type.getDisplayText(selectedAttendanceType != type),
                                    color = if (type == selectedAttendanceType) MaterialTheme.colorScheme.onPrimary else type.getColor(
                                        isDarkMode
                                    )
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

// Preview removed - LectureItem requires database objects that can't be easily mocked

data class LectureInfo(
    val id: Int = 0,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val subjectName: String,
    val locationName: String,
    val attendancePer: Int,
//    val task: TaskItem,
    val reminder: List<LocalDateTime>,
    val presentType: PresentType,
) {
    val attendanceWarning: String = "You can miss this lecture"
    val isHappening: Boolean = true
}

data class TaskItem(
    val id: Int,
    val task: String,
    val reminder: LocalDateTime
)

enum class PresentType(
    val lightColor: Color,
    val darkColor: Color,
    val title: String
) {
    NOT_COUNTED(
        lightColor = Color(0xFF616161),   // Grey 700
        darkColor = Color(0xFFBDBDBD),    // Light Grey
        title = "Not Counted"
    ),

    CANCELLED(
        lightColor = Color(0xFFF9A825),   // Amber 800
        darkColor = Color(0xFFFFE082),    // Light Amber
        title = "Cancelled"
    ),

    ABSENT(
        lightColor = Color(0xFFC62828),   // Red 800
        darkColor = Color(0xFFEF9A9A),    // Light Red
        title = "Absent"
    ),

    PRESENT(
        lightColor = Color(0xFF2E7D32),   // Green 800
        darkColor = Color(0xFF81C784),    // Light Green
        title = "Present"
    );

    fun getColor(isDark: Boolean): Color {
        return if (isDark) darkColor else lightColor
    }

    fun getDisplayText(isCompact: Boolean): String {
        return if (isCompact) title.first().toString() else title
    }
}

// Helper functions
private fun minutesToTimeString(minutes: Int): String {
    val hour = minutes / 60
    val minute = minutes % 60
    val period = if (hour < 12) "am" else "pm"
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return String.format("%d:%02d %s", displayHour, minute, period)
}

private fun com.reyaz.feature.attendance.data.local.model.AttendanceStatus.toPresentType(): PresentType {
    return when (this) {
        com.reyaz.feature.attendance.data.local.model.AttendanceStatus.PRESENT -> PresentType.PRESENT
        com.reyaz.feature.attendance.data.local.model.AttendanceStatus.ABSENT -> PresentType.ABSENT
        com.reyaz.feature.attendance.data.local.model.AttendanceStatus.CANCELLED -> PresentType.CANCELLED
    }
}