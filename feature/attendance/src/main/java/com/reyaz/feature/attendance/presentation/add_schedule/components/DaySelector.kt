package com.reyaz.feature.attendance.presentation.add_schedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyaz.core.ui.extensions.calculateHorizontalItemDimensions
import com.reyaz.core.ui.helper.debounceClickable
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.getDayAbbreviation
import kotlinx.datetime.DayOfWeek

@Composable
fun DaySelector(
    selectedDay: DayOfWeek, onDaySelected: (DayOfWeek) -> Unit
) {
    val days = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    // Use the extension function to calculate item dimensions
    val dimensions = calculateHorizontalItemDimensions(
        visibleItems = 5,
        itemSpacing = 8.dp,
        horizontalPadding = 32.dp
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimensions.itemSpacing),
        modifier = Modifier.Companion.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        items(
            items = days
        ) { day ->
            DayButton(
                day = getDayAbbreviation(day),
                isSelected = day == selectedDay,
                onClick = { onDaySelected(day) },
                modifier = Modifier.Companion.width(dimensions.itemWidth)
            )
        }
    }
}