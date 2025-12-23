package com.reyaz.feature.attendance.presentation.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.attendance.presentation.components.HorizontalCalendar
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.asTimeSource
import kotlinx.datetime.atTime
import kotlinx.datetime.todayIn
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = koinViewModel()

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


    }
}

@Composable
fun LectureList(modifier: Modifier) {
    LectureItem()

}

@Composable
fun LectureItem(
    modifier: Modifier = Modifier,
//    lectureInfo: LectureInfo,
) {
    val attendancePerColor = MaterialTheme.colorScheme.primary
    val task: String? = null
    val locationName: String? = "Faculty of Engg. & Technology"
    val taskColor = MaterialTheme.colorScheme.outline
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(124.dp)
    ) {
        Row() {
            Text(
//                modifier = Modifier.align(Alignment.TopStart),
                text = "9:35 am\n-\n10:37 pm",
                textAlign = TextAlign.Center,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(10.dp)
            ) {
                Column() {
                    Row(
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

                        Spacer(Modifier.weight(1f))
//                        Spacer(Modifier.width(24.dp))

                        Column(
                            Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = "Engg. Mathematics III",
                                fontSize = 18.sp
                            )
                            // location
                            locationName?.let {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "task icon",
                                        tint = taskColor,
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = it,
                                        color = taskColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(Modifier.weight(1f))

                                }
                            }
                        }
//                        Spacer(Modifier.weight(1f))
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "lecture reminder"
                        )

                    }

                    Spacer(Modifier.height(12.dp))

                    // task
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
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
                        )
                        Spacer(Modifier.weight(1f))

                    }
                }
            }

        }
    }

}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LectureItemPreview() {
    Column(Modifier.padding(16.dp)) {
        LectureItem()
        LectureItem()
    }
}

data class LectureInfo(
    val id: Int = 0,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val subjectName: String,
    val locationName: String,
    val attendancePer: Int,
    val attendanceStatus: String,
    val task: TaskItem,
    val reminder: List<LocalDateTime>,
    val presentType: PresentType,
) {
    val currentTime = LocalTime
//    val isHappening: Boolean = if (currentTime.>= startTime && currentTime <= endTime)
}

data class TaskItem(
    val id: Int,
    val task: String,
    val reminder: LocalDateTime
)

enum class PresentType(
    color: Color = Color(0xFF000000),
) {
    PRESENT(
//        color = if (isDarkTheme)MaterialTheme.colorScheme.primary
    ),
    ABSENT(
//        color = MaterialTheme.colorScheme.error
    ),
    CANCELLED(
//        color = MaterialTheme.colorScheme.surfaceVariant
    ),
    NOT_COUNTED
}