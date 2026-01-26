package com.reyaz.feature.attendance.presentation.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ModeStandby
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.components.SingleLinePopText
import com.reyaz.core.ui.helper.debounceClickable
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.domain.model.ScheduleLectureUiModel
import com.reyaz.feature.attendance.utils.TimeUtils

@Composable
fun LectureItemComponents(
    lectureData: ScheduleLectureUiModel,
    onAttendanceTypeSelected: (AttendanceStatus) -> Unit,
    modifier: Modifier = Modifier,
    isLastItem: Boolean = false
) {
    val percentage = lectureData.attendancePercentage
    val statusColor =
        if (percentage >= 75) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val warningColor =
        if (percentage >= 75) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error        // todo: choose based on
    val isDarkMode = isSystemInDarkTheme()

    val selectedAttendanceType = lectureData.attendanceStatus ?: AttendanceStatus.NOT_COUNTED

    // Convert minutes to time string
    val startTime = TimeUtils.formatMinutesTo12Hour(lectureData.startTimeMinute)
    val endTime = TimeUtils.formatMinutesTo12Hour(lectureData.endTimeMinute)
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
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = if (isLastItem) {
                                arrayOf(
                                    0.0f to MaterialTheme.colorScheme.onBackground,
                                    0.7f to MaterialTheme.colorScheme.onBackground,
                                    1.0f to Color.Transparent
                                )
                            } else {
                                arrayOf(
                                    0.0f to MaterialTheme.colorScheme.onBackground,
                                    1.0f to MaterialTheme.colorScheme.onBackground
                                )
                            }
                        )
                    )
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
                            .background(statusColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "${percentage}%",
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }

//                        Spacer(Modifier.weight(1f))
                    Spacer(Modifier.width(4.dp))

                    Column(
                        Modifier.padding(horizontal = 8.dp)
                    ) {
                        lectureData?.subjectName?.let {
                            SingleLinePopText(
                                text = it,
                                fontSize = 18.sp,
                                shouldShowPopup = true
                            )
                        }
                        // location name
                        lectureData.locationName?.let {
                            Spacer(Modifier.height(4.dp))
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
                                SingleLinePopText(
                                    text = it,
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                    shouldShowPopup = true
                                )
                            }
                        }
//                        Spacer(Modifier.height(4.dp))
                        // warning
                        Spacer(Modifier.height(4.dp))
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
                            SingleLinePopText(
                                text = lectureData.attendanceWarning,
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
                        AttendanceStatus.entries.forEach { type ->
                            Box(
                                modifier = Modifier
                                    .clip(
                                        shape = CircleShape
//                                            shape = RoundedCornerShape(50)
                                    )
                                    .debounceClickable {
                                        if (type == selectedAttendanceType) return@debounceClickable
                                        onAttendanceTypeSelected(type)
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
                                Text(
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