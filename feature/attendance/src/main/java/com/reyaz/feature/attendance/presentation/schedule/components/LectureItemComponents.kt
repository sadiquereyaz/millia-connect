package com.reyaz.feature.attendance.presentation.schedule.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.components.SingleLineText
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.utils.TimeUtils.minutesToTimeString
import com.reyaz.feature.attendance.utils.toPresentType
import kotlin.random.Random

@Composable
fun LectureItemComponents(
    lectureData: LectureAttendanceWithSubject,
    onAttendanceTypeSelected: (Long, AttendanceStatus) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    val attendancePerColor =
        if (Random.Default.nextBoolean()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val task: String? = null
//    val locationName: String? = null // TODO: Add location support
    val locationName: String? =
        "Faculty of Engineering and Technology" // TODO: Add location support
    val taskColor = MaterialTheme.colorScheme.outline
    val warningColor =
        if (Random.Default.nextBoolean()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val isDarkMode = isSystemInDarkTheme()

    // Convert AttendanceStatus to PresentType
    val selectedAttendanceType =
        lectureData.attendance?.status?.toPresentType() ?: AttendanceStatus.NOT_COUNTED

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
            textAlign = TextAlign.Companion.Center,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            modifier = Modifier.Companion
//                .background(Color.Red)
                .width(60.dp)
        )

        // line left space
        Spacer(Modifier.Companion.width(4.dp))

        Column(
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.Companion
                    .clip(CircleShape)
                    .size(6.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )

            // vertical line
            Box(
                modifier = Modifier.Companion
                    .fillMaxHeight()
                    .width(0.6.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )

        }
        // line right space
        Spacer(Modifier.Companion.width(8.dp))

        // subject info box
        Box(
            Modifier.Companion.padding(
                bottom = 16.dp
            )

        ) {
            Column(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    // attendance percent circle
                    Box(
                        modifier = Modifier.Companion
                            .clip(CircleShape)
                            .size(48.dp)
                            .background(attendancePerColor.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Companion.Center,
                    ) {
                        Text(
                            text = "64%",
                            fontWeight = FontWeight.Companion.Bold,
                            color = attendancePerColor
                        )
                    }

//                        Spacer(Modifier.weight(1f))
                    Spacer(Modifier.Companion.width(4.dp))

                    Column(
                        Modifier.Companion.padding(horizontal = 8.dp)
                    ) {
                        SingleLineText(
                            text = lectureData.subject.name,
                            fontSize = 18.sp
                        )
                        // location
                        locationName?.let {
//                            Spacer(Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.Companion.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.Companion.size(12.dp),
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "task icon",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                )
                                Spacer(Modifier.Companion.width(4.dp))
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
                            verticalAlignment = Alignment.Companion.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.Companion.size(12.dp),
                                imageVector = Icons.Default.ModeStandby,
                                contentDescription = "task icon",
                                tint = warningColor,
                            )
                            Spacer(Modifier.Companion.width(2.dp))
                            SingleLineText(
                                text = "You can miss this lecture",
                                color = warningColor,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                modifier = Modifier.Companion.padding(start = 4.dp)
                            )
                        }
                    }
                    Spacer(Modifier.Companion.weight(1f))
                    /*Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "lecture reminder"
                    )*/

                }

                Spacer(Modifier.Companion.height(12.dp))

                // task
                Row(
                    verticalAlignment = Alignment.Companion.CenterVertically
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
                    Spacer(Modifier.Companion.weight(1f))
                    // atttemdance types
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        AttendanceStatus.entries.forEach { type ->
                            Box(
                                modifier = Modifier.Companion
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
                                            Modifier.Companion
                                                .height(24.dp)
                                                .background(type.getColor(isDarkMode))
                                                .padding(horizontal = 8.dp)
                                        } else {
                                            Modifier.Companion.size(24.dp)
                                        }
                                    ),
                                contentAlignment = Alignment.Companion.Center,
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
                Spacer(Modifier.Companion.height(4.dp))
            }
        }
    }
}