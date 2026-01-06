package com.reyaz.feature.attendance.presentation.schedule.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.extensions.dottedBorder
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.domain.model.AttendanceStatus

@Composable
fun LectureList(
    lectures: List<LectureAttendanceWithSubject>,
    onAttendanceTypeSelected: (Long, AttendanceStatus) -> Unit,
    modifier: Modifier = Modifier.Companion,
    onAddSchedule: () -> Unit,
) {
    if (lectures.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Companion.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize(0.8f)
                    .dottedBorder()
                    .clickable {
                        onAddSchedule()
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
            ) {
                Icon(
                    Icons.Default.Add,
                    "add schedule",
                    modifier = Modifier.Companion
                        .size(68.dp)
                        .border(1.dp, color = MaterialTheme.colorScheme.onSurface, CircleShape),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.Companion.height(16.dp))
                Text(
                    modifier = Modifier.Companion,
                    text = "No Schedule found for this day,\nClick to add.",
                    textAlign = TextAlign.Companion.Center,
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
                modifier = Modifier.Companion.padding(16.dp, 8.dp)
            ) {
                items(
                    count = lectures.size,
                    key = { index -> lectures[index].lecture.lectureId }
                ) { index ->
                    LectureItemComponents(
                        lectureData = lectures[index],
                        onAttendanceTypeSelected = onAttendanceTypeSelected
                    )
                }
                item {
                    Spacer(Modifier.Companion.height(ButtonDefaults.MinHeight + 32.dp))
                }
            }

            // Show FAB when last item is visible
            if (isLastItemVisible) {
                SmallFloatingActionButton(
                    onClick = onAddSchedule,
                    modifier = Modifier.Companion
                        .align(Alignment.Companion.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.Companion.padding(16.dp, 16.dp)
                    ) {
                        Text(
                            text = "Edit/Modify",
                            fontWeight = FontWeight.Companion.SemiBold,
                            fontSize = 16.sp
                        )
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