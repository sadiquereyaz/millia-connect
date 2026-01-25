package com.reyaz.feature.attendance.presentation.add_schedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.extensions.dottedBorder
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.utils.TimeUtils

@Composable
fun UpdateScreenLectureCard(
    lecSlot: LectureAttendanceWithSubject,
    onUpdate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    isInConflict: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .then(
                if (isInConflict) {
                    Modifier.dottedBorder(
                        color = MaterialTheme.colorScheme.error,
                        strokeWidth = 1.dp,
                        dashOff = 0f,
                    )
                } else {
                    Modifier.dottedBorder()
                }
            )
            .padding(horizontal = 16.dp, 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = lecSlot.subject.subjectName, fontWeight = FontWeight.SemiBold, fontSize = 16.sp
            )
            Text(
                "${TimeUtils.formatMinutesTo12Hour(lecSlot.lecture.startTimeMinutes)} - ${
                    TimeUtils.formatMinutesTo12Hour(
                        lecSlot.lecture.endTimeMinutes
                    )
                }",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Medium,
            )
        }
        Row {
            IconButton(onClick = onUpdate) {
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