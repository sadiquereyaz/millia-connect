package com.reyaz.feature.attendance.presentation.add_schedule.presentation

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
import com.reyaz.feature.attendance.data.local.model.LectureWithSubject

@Composable
fun LectureCard(
    lecture: LectureWithSubject,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .dottedBorder()
            .padding(horizontal = 16.dp, 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Column(modifier = Modifier.Companion.weight(1f)) {
            Text(
                lecture.subject.name, fontWeight = FontWeight.Companion.SemiBold, fontSize = 16.sp
            )
            Text(
                "${formatMinutesToTime(lecture.lecture.startTimeMinutes)} - ${
                    formatMinutesToTime(
                        lecture.lecture.endTimeMinutes
                    )
                }",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                fontWeight = FontWeight.Companion.Medium,
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