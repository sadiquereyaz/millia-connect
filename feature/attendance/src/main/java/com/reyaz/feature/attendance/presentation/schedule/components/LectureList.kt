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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
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
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.domain.model.ScheduleLectureUiModel

@Composable
fun LectureList(
    lectures: List<ScheduleLectureUiModel>,
    onAttendanceTypeSelected: (Long?, Long, AttendanceStatus) -> Unit,
    modifier: Modifier = Modifier,
    navigateToAddSchedule: () -> Unit,
    isLoading: Boolean
) {
    if (lectures.isEmpty() && !isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize(0.8f)
                    .dottedBorder()
                    .clickable {
                        navigateToAddSchedule()
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
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastIndex
                lastVisibleItemIndex > lectures.size
            }
        }

        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(16.dp, 8.dp)
            ) {
                items(
                    items = lectures,
                    key = { it.lectureId }      // todo: check lecture id is not changing when attendance is updated
                ) { lecSlot ->
                    LectureItemComponents(
                        lectureData = lecSlot,
                        onAttendanceTypeSelected = { status->
                            onAttendanceTypeSelected(lecSlot.attendanceId, lecSlot.lectureId, status)
                        },
                        isLastItem = lectures.last() == lecSlot
                    )
                }
                items(
                    2
                ){
                    Spacer(Modifier.height(32.dp))
                }
            }

            // Show FAB when last item is visible
            if (isLastItemVisible) {
                SmallFloatingActionButton(
                    onClick = navigateToAddSchedule,
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
                        Text(
                            text = "Edit/Modify",
                            fontWeight = FontWeight.SemiBold,
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