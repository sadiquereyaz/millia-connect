package com.reyaz.feature.attendance.presentation.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.common.utils.extensions.toCapSmall
import com.reyaz.feature.attendance.presentation.schedule.ScheduleViewModel.Companion.CENTER_INDEX
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Composable
fun HorizontalCalendar(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    totalAttendancePer: Int?,
    targetPer: Int?,
    todayDate: LocalDate    // todo: remove
) {
    val baseDate = remember { todayDate }
    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = CENTER_INDEX + 2)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${
                        selectedDate.month.name.take(3).toCapSmall()
                    }, ${selectedDate.year}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )   // todo: onclick -> show calendar to select date.

                targetPer?.let { targetPer ->
                    totalAttendancePer?.let {
                        Box(
                            modifier = Modifier
                                .border(
                                    shape = RoundedCornerShape(24),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (totalAttendancePer < targetPer) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    )
                                )
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Overall: ${totalAttendancePer}%",
                                color = if (totalAttendancePer < targetPer) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(ScheduleViewModel.TOTAL_ITEMS) { index ->

                    val date = remember(index) {
                        baseDate.plus(index - CENTER_INDEX, DateTimeUnit.DAY)
                    }

                    CalendarItem(
                        isSelected = date == selectedDate,
                        isToday = date == todayDate,
                        date = date,
                        onDateSelected = { onDateSelected(date) }
                    )
                }
            }
        }
    }

}

@Composable
fun CalendarItem(
    isSelected: Boolean = false,
    isToday: Boolean = false,
    date: LocalDate,
    onDateSelected: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
    val backgroundColor =
        if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface

    // todo: cal width of each item so that only 5 item can fit at a time.
    Surface(
        shape = RoundedCornerShape(24),
        onClick = onDateSelected,
        color = backgroundColor,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfWeek.name.take(3).toCapSmall(),
                fontSize = 12.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

