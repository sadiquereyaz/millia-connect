package com.reyaz.core.ui.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Extension function to add a dotted border to any composable element
 *
 * @param color Color of the dotted border
 * @param strokeWidth Width of the border stroke
 * @param cornerRadius Corner radius of the border
 * @param dashOn Length of each dash in pixels
 * @param dashOff Gap between dashes in pixels
 *
 * Example usage:
 * ```
 * Box(modifier = Modifier.dottedBorder()) {
 *     Text("Content")
 * }
 * ```
 */
fun Modifier.dottedBorder(
    color: Color = Color.Companion.Gray,
    strokeWidth: Dp = 1.dp,
    cornerRadius: Dp = 12.dp,
    dashOn: Float = 10f,
    dashOff: Float = 8f
): Modifier = this.drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.Companion.dashPathEffect(
                floatArrayOf(dashOn, dashOff)
            )
        ),
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}