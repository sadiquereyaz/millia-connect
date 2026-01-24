package com.reyaz.feature.attendance.presentation.graph.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.reyaz.feature.attendance.presentation.graph.model.DonutChartItem
import com.reyaz.feature.attendance.presentation.graph.utils.ColorUtils
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DonutChart(
    data: List<DonutChartItem>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 100f
) {
    val isSystemInDarkMode: Boolean = isSystemInDarkTheme()
    val total = data.sumOf { it.value.toDouble() }.toFloat()
    val overallPercent = ((data[0].value / total) * 100).toInt()

    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        val canvasSize = size.minDimension
        val radius = (canvasSize)/5 * 2
        val (midX, midY) = size.width / 2 to size.height / 2
        val topLeft = Offset(midX-radius, midY-radius)

        var startAngle = -90f

        val labelPaint = Paint().apply {
            textSize = 28f
            color = onSurfaceColor.toArgb()
            isAntiAlias = true
        }
        val canvas = drawContext.canvas.nativeCanvas

        data.forEachIndexed { index, item ->
            val sweepAngle = (item.value / total) * 360f

            // Arc
            drawArc(
                color = ColorUtils.getColor(isSystemInDarkMode, index + 3),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth),
                size = Size(radius*2, radius*2),
                topLeft = topLeft
            )

            // Label
            val midAngle = startAngle + sweepAngle / 2
            val angleRad = Math.toRadians(midAngle.toDouble())

            val labelRadius = radius + (strokeWidth / 1.4f) + 24f

            val labelX = midX + labelRadius * cos(angleRad).toFloat()
            val labelY = midY + labelRadius * sin(angleRad).toFloat()

            labelPaint.textAlign =
                if (labelX < midX) Paint.Align.RIGHT else Paint.Align.LEFT

            val percent = ((item.value / total) * 100).toInt()

            val fm = labelPaint.fontMetrics
            val lineHeight = fm.descent - fm.ascent

            drawContext.canvas.nativeCanvas.drawText(
                item.label,
                labelX,
                labelY,
                labelPaint
            )

            drawContext.canvas.nativeCanvas.drawText(
                "${percent}%",
                labelX,
                labelY + lineHeight,
                labelPaint
            )


            startAngle += sweepAngle
        }

        // Center text
        val centerPaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 72f
            color = onSurfaceColor.toArgb()
            isFakeBoldText = true
        }
        val innerLabelPaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 36f
            color = onSurfaceColor.copy(alpha = 0.8f).toArgb()
        }
        canvas.drawText("${overallPercent}%",  midX, midY, centerPaint)
        canvas.drawText("Overall", midX, midY + 44f, innerLabelPaint)
    }
}
