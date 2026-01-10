package com.reyaz.feature.attendance.presentation.add_schedule.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reyaz.core.ui.extensions.calculateHorizontalItemDimensions
import com.reyaz.core.ui.helper.debounceClickable
import com.reyaz.feature.attendance.presentation.add_schedule.presentation.getDayAbbreviation
import com.reyaz.feature.attendance.utils.TimeUtils
import kotlinx.datetime.DayOfWeek

@Composable
fun DaySelector(
    selectedDay: DayOfWeek, onDaySelected: (DayOfWeek) -> Unit
) {

    // Using extension function to calculate item dimensions
    val dimensions = calculateHorizontalItemDimensions(
        visibleItems = 5,
        itemSpacing = 8.dp,
        horizontalPadding = 32.dp
    )

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = TimeUtils.allDaysOfWeek().indexOf(selectedDay)
    )

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(dimensions.itemSpacing),
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(
            items = TimeUtils.allDaysOfWeek()
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