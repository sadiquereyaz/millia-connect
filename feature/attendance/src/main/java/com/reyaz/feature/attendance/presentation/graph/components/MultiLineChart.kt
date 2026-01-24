package com.reyaz.feature.attendance.presentation.graph.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation
import com.reyaz.core.common.utils.extensions.StringUtils.getShortForm
import com.reyaz.feature.attendance.presentation.graph.model.GraphData
import com.reyaz.feature.attendance.presentation.graph.model.dummyGraphData1
import com.reyaz.feature.attendance.presentation.graph.utils.buildSmoothPath

@Composable
fun MultiLineChart(
    modifier: Modifier = Modifier,
    graphData: GraphData,
) {
    val outline = MaterialTheme.colorScheme.outline
    val onSurface = MaterialTheme.colorScheme.onSurface
    val error = MaterialTheme.colorScheme.error

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (graphData.isValid) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {

                // ---------- Layout ----------
                val padding = 40f
                val topPadding = 40f
                val endPadding = 40f
                val xAxisBottomPadding = 80f
                val yAxisStartPadding = 80f

                val chartWidth = size.width - yAxisStartPadding - endPadding    // width of x axis
                val chartHeight =
                    size.height - topPadding - xAxisBottomPadding     // height of y axis

                val originY = size.height - xAxisBottomPadding
                val stepX = chartWidth / (graphData.subjects.size)

                // ---------- Y scale ----------

                val minY = 0f
                val maxY = 100f
                val yDivisionCount = 5
                val yStepPx = chartHeight / yDivisionCount

                fun pointFor(subjectIndex: Int, percentage: Float = 0f): Offset {
                    val x =
                        yAxisStartPadding + stepX / 2 /*for starting x space*/ + stepX * subjectIndex
                    val y = originY - (percentage / maxY) * chartHeight
                    return Offset(x, y)
                }

                // ---------- Axes ----------
                // X axis
                drawLine(
                    color = outline,
                    start = Offset(yAxisStartPadding, originY),
                    end = Offset(size.width - endPadding, originY),
                    strokeWidth = 2f
                )

                // Y axis
                drawLine(
                    color = outline,
                    start = Offset(yAxisStartPadding, topPadding),
                    end = Offset(yAxisStartPadding, originY),
                    strokeWidth = 2f
                )

                // ---------- Y grid + labels ----------
                (0..yDivisionCount).forEach { i ->
                    val y = originY - i * yStepPx
                    val yLabelValue = (i * (maxY / yDivisionCount)).toInt()

                    // dotted horizontal line
                    drawLine(
                        color = outline.copy(alpha = 0.6f),
                        start = Offset(yAxisStartPadding, y),
                        end = Offset(size.width - endPadding, y),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f))
                    )

                    // y axis label
                    drawContext.canvas.nativeCanvas.drawText(
                        "$yLabelValue%", yAxisStartPadding - padding / 4, y + 8, Paint().apply {
                            textAlign = Paint.Align.RIGHT
                            textSize = 24f
                            color = onSurface.toArgb()
                        })
                }

                // ---------- DRAW LINES (one per month) ----------
                dummyGraphData1.lineData.forEach {
                    val points = it.percentages.mapIndexed { index, percentage ->
                        val p = pointFor(index, percentage)

                        // draw dot
                        drawCircle(
                            color = it.lineColor, radius = 6f, center = p
                        )

                        // percent line label   (Todo: make it visible only when user clicks
                        drawContext.canvas.nativeCanvas.drawText(
                            "${percentage.toInt()}%", p.x, p.y - 10, Paint().apply {
                                textAlign = Paint.Align.CENTER
                                textSize = 22f
                                color = onSurface.toArgb()
                            })

                        // month name
                        if (index == it.percentages.lastIndex) {
                            drawContext.canvas.nativeCanvas.drawText(
//                            month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                it.month, p.x + 30f, p.y + 8f, Paint().apply {
                                    textAlign = Paint.Align.LEFT
                                    textSize = 22f
                                    color = onSurface.toArgb()
                                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                                })
                        }

                        p
                    }
                    val smoothPath = buildSmoothPath(points)

                    drawPath(
                        path = smoothPath, color = it.lineColor, style = Stroke(
                            width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round
                        )
                    )
                }


                // ---------- X axis labels ----------
                dummyGraphData1.subjects.forEachIndexed { index, subject ->
                    val x = pointFor(index).x
                    drawLine(
                        color = outline,
                        start = Offset(x, originY - 8),
                        end = Offset(x, originY + 8),
                        strokeWidth = 2f
                    )

                    drawContext.canvas.nativeCanvas.withRotation(
                        -45f, x, originY
                    ) {
                        drawText(subject.takeIf { it.length <= 7 } ?: subject.getShortForm(4),
                            x,
                            originY + padding,
                            Paint().apply {
                                textAlign = Paint.Align.RIGHT
                                textSize = 22f
                                color = onSurface.toArgb()
                                isAntiAlias = true
                            })
                    }
                }
                // todo: when any subject clicked, show their percentage along with month name
            }
        } else {
            Text("Invalid Graph Data", color = error)
        }
    }
}