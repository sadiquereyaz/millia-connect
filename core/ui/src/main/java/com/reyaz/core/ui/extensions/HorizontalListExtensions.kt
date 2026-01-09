package com.reyaz.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Data class to hold calculated dimensions for horizontal list items
 *
 * @property itemWidth The calculated width for each item
 * @property itemSpacing The spacing between items
 * @property totalWidth The total available width
 */
data class HorizontalListDimensions(
    val itemWidth: Dp,
    val itemSpacing: Dp,
    val totalWidth: Dp
)

/**
 * Calculate item width for a horizontal list based on screen width and visible items
 *
 * This function helps create evenly-sized items in a horizontal LazyRow or Row,
 * taking into account the screen width, desired number of visible items, spacing, and padding.
 *
 * @param visibleItems Number of items that should be visible on screen at once (default: 5)
 * @param itemSpacing Spacing between items (default: 8.dp)
 * @param horizontalPadding Total horizontal padding (start + end) (default: 32.dp)
 *
 * @return HorizontalListDimensions containing calculated itemWidth, itemSpacing, and totalWidth
 *
 * Example usage:
 * ```
 * @Composable
 * fun DaySelector() {
 *     val dimensions = calculateHorizontalItemDimensions(
 *         visibleItems = 5,
 *         itemSpacing = 8.dp
 *     )
 *
 *     LazyRow(
 *         horizontalArrangement = Arrangement.spacedBy(dimensions.itemSpacing)
 *     ) {
 *         items(days) { day ->
 *             DayItem(
 *                 modifier = Modifier.width(dimensions.itemWidth)
 *             )
 *         }
 *     }
 * }
 * ```
 */
@Composable
fun calculateHorizontalItemDimensions(
    visibleItems: Int = 5,
    itemSpacing: Dp = 8.dp,
    horizontalPadding: Dp = 32.dp
): HorizontalListDimensions {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // Calculate item width: (total width - padding - all gaps between items) / number of items
    val itemWidth = (screenWidth - horizontalPadding - (itemSpacing * (visibleItems - 1))) / visibleItems

    return HorizontalListDimensions(
        itemWidth = itemWidth,
        itemSpacing = itemSpacing,
        totalWidth = screenWidth
    )
}

/**
 * Alternative version that allows custom total width instead of using screen width
 *
 * @param totalWidth The total width available for the list
 * @param visibleItems Number of items that should be visible at once
 * @param itemSpacing Spacing between items
 *
 * @return HorizontalListDimensions containing calculated dimensions
 */
fun calculateHorizontalItemDimensions(
    totalWidth: Dp,
    visibleItems: Int = 5,
    itemSpacing: Dp = 8.dp
): HorizontalListDimensions {
    val itemWidth = (totalWidth - (itemSpacing * (visibleItems - 1))) / visibleItems

    return HorizontalListDimensions(
        itemWidth = itemWidth,
        itemSpacing = itemSpacing,
        totalWidth = totalWidth
    )
}