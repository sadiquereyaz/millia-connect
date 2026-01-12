package com.reyaz.feature.attendance.presentation.records.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    attendanceData: List<SubjectPercentage> = listOf(
        SubjectPercentage("Math", 85f),
        SubjectPercentage("Physics", 72f),
        SubjectPercentage("Chemistry", 90f),
        SubjectPercentage("English", 95f),
        SubjectPercentage("CS", 60f),
        SubjectPercentage("Biology", 88f)
    ),
) {
    Column(
        modifier = modifier.fillMaxSize()

    ) {
        val primary = MaterialTheme.colorScheme.primary
        val onPrimary = MaterialTheme.colorScheme.onPrimary
        val primaryContainer = MaterialTheme.colorScheme.primaryContainer
        val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer

        val secondary = MaterialTheme.colorScheme.secondary
        val onSecondary = MaterialTheme.colorScheme.onSecondary
        val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer
        val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer

        val tertiary = MaterialTheme.colorScheme.tertiary
        val onTertiary = MaterialTheme.colorScheme.onTertiary
        val tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
        val onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer
        val background = MaterialTheme.colorScheme.background
        val onBackground = MaterialTheme.colorScheme.onBackground

        val surface = MaterialTheme.colorScheme.surface
        val onSurface = MaterialTheme.colorScheme.onSurface

// Variants are great for subtle grid lines or secondary text labels
        val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
        val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
        val outline = MaterialTheme.colorScheme.outline
        val outlineVariant = MaterialTheme.colorScheme.outlineVariant // Even more subtle
        val error = MaterialTheme.colorScheme.error
        val onError = MaterialTheme.colorScheme.onError
        val errorContainer = MaterialTheme.colorScheme.errorContainer
        val onErrorContainer = MaterialTheme.colorScheme.onErrorContainer

// For "floating" elements or high-contrast tooltips
        val inverseSurface = MaterialTheme.colorScheme.inverseSurface
        val inverseOnSurface = MaterialTheme.colorScheme.inverseOnSurface
        val inversePrimary = MaterialTheme.colorScheme.inversePrimary

// Scrim is used for the darkened background behind modals
        val scrim = MaterialTheme.colorScheme.scrim

        val minValue = attendanceData.minOf { it.percentage }
        val maxValue = attendanceData.maxOf { it.percentage }


        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val padding = 40f
            val topPadding = 40f
            val endPadding = 40f
            val xAxisBottomPadding = 80f
            val yAxisStartPadding = 80f
            val chartWidth = size.width - yAxisStartPadding - endPadding
            val chartHeight = size.height - topPadding - xAxisBottomPadding
            val stepX = chartWidth / (attendanceData.size + 1)
            val originY = size.height - xAxisBottomPadding
            val yAxisHeight = originY - topPadding
            val yStep = yAxisHeight / 10f

            fun pointFor(index: Int, percentage: Float): Offset {
                val x = yAxisStartPadding + stepX + stepX * index   // 1 stepX (for blank grid)
                val y =
                    size.height - xAxisBottomPadding - (percentage / 100f) * chartHeight        // todo: check calculation
                return Offset(x, y)
            }

            // X-AXIS
            drawLine(
                color = outline,
                start = Offset(
                    x = yAxisStartPadding - padding / 2,        // extra x axis at origin
                    y = size.height - xAxisBottomPadding
                ),
                end = Offset(
                    x = size.width - endPadding,
                    y = size.height - xAxisBottomPadding
                ),
                2f
            )
            // Y-AXIS
            drawLine(
                color = outline,
                start = Offset(
                    x = yAxisStartPadding,
                    y = topPadding,
                ),
                end = Offset(
                    x = yAxisStartPadding,
                    y = size.height - xAxisBottomPadding + padding / 2   // extra y axis at origin
                ),
                strokeWidth = 2f
            )

            // Line
            val path = Path()
            attendanceData.forEachIndexed { index, item ->
                val p = pointFor(index, item.percentage)
                if (index == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
            }
            drawPath(path, primary, style = Stroke(4f))

            // y axis label + divider
            (1..10).forEach {
                drawLine(
                    color = outline.copy(alpha = 0.8f),
                    start = Offset(
                        x = yAxisStartPadding,
                        y = originY - (it * yStep)
                    ),
                    end = Offset(
                        x = size.width - endPadding,
                        y = originY - (it * yStep)
                    ),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f))
                )

                val paint = Paint().apply {
                    textSize = 26f
                    textAlign = Paint.Align.RIGHT
                }

                val fm = paint.fontMetrics
                val targetY = originY - (it * yStep)
                val centeredY = targetY - (fm.ascent + fm.descent) / 2
                drawContext.canvas.nativeCanvas.drawText(
                    "${it * 10}%",
                    yAxisStartPadding - padding / 4,
                    centeredY,
                    android.graphics.Paint().apply {
                        textAlign = android.graphics.Paint.Align.RIGHT
                        textSize = 26f
                        color = onSurface.toArgb()
                    }
                )
            }

            // X-axis Points + labels
            attendanceData.forEachIndexed { index, item ->
                val p = pointFor(index, item.percentage)

                // coordinate dot
                drawCircle(error, 6f, p)

                // on x-axis vertical indicator
                drawLine(
                    color = outline,
                    start = Offset(
                        x = p.x,
                        y = size.height - xAxisBottomPadding - padding / 4
                    ),
                    end = Offset(
                        x = p.x,
                        y = size.height - xAxisBottomPadding + padding / 4
                    ),
                    strokeWidth = 2f
                )

                // point coordinate text
                drawContext.canvas.nativeCanvas.drawText(
                    "${item.percentage.toInt()}%",
                    p.x,
                    p.y - 12,
                    Paint().apply {
                        textAlign = Paint.Align.CENTER
                        textSize = 26f
                        color = onSurface.toArgb()
                    }
                )

                // x-axis label
                val nativeCanvas = drawContext.canvas.nativeCanvas
                nativeCanvas.withRotation(
                    -45f,               // angle
                    p.x,                // pivot X (text X)
                    size.height - xAxisBottomPadding // pivot Y (text Y)
                ) {
                    drawText(
                        item.subject,
                        p.x,
                        size.height - xAxisBottomPadding + padding,
                        Paint().apply {
                            textAlign = Paint.Align.RIGHT
                            textSize = 24f
                            color = onSurface.toArgb()
                            isAntiAlias = true
                        }
                    )
                }
            }
        }
    }
}

data class SubjectPercentage(
    val subject: String,
    val percentage: Float // 0f to 100f
)