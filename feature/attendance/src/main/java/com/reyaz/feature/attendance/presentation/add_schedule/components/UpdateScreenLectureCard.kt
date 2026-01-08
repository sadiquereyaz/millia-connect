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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.extensions.dottedBorder
import com.reyaz.feature.attendance.domain.model.LectureItem
import com.reyaz.feature.attendance.utils.TimeUtils.formatMinutesToTime

@Composable
fun UpdateScreenLectureCard(
    lecture: LectureItem,
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
                        strokeWidth = 2.dp,
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
                text = lecture.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp
            )
            Text(
                "${formatMinutesToTime(lecture.startTimeMinute)} - ${
                    formatMinutesToTime(
                        lecture.endTimeMinute
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