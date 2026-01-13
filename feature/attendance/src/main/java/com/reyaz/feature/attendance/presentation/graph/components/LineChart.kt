package com.reyaz.feature.attendance.presentation.graph.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation
import com.reyaz.core.common.utils.extensions.StringUtils.getShortForm
import kotlinx.datetime.Month
import com.reyaz.feature.attendance.presentation.graph.utils.ColorUtils
import java.time.format.TextStyle
import java.util.Locale
@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    attendanceData: List<SubjectWithMonthlyPercentage> = getDummySubjectPercentages(),
) {
    val outline = MaterialTheme.colorScheme.outline
    val onSurface = MaterialTheme.colorScheme.onSurface
    val error = MaterialTheme.colorScheme.error

    Column(modifier = modifier.fillMaxSize()) {

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
            val chartHeight = size.height - topPadding - xAxisBottomPadding     // height of y axis

            val originY = size.height - xAxisBottomPadding
            val stepX = chartWidth / (attendanceData.size)

            // ---------- Y scale ----------
            val allPercentages =
                attendanceData.flatMap { it.monthlyAttendance }.map { it.percentage }

            val minY = 0f
            val maxY = 100f
            val yDivisions = 5
            val yStepPx = chartHeight / yDivisions

            fun pointFor(subjectIndex: Int, percentage: Float = 0f): Offset {
                val x = yAxisStartPadding + stepX/2 /*for starting x space*/ + stepX * subjectIndex
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
            (0..yDivisions).forEach { i ->
                val y = originY - i * yStepPx
                val value = (i * (maxY / yDivisions)).toInt()

                drawLine(
                    color = outline.copy(alpha = 0.6f),
                    start = Offset(yAxisStartPadding, y),
                    end = Offset(size.width - endPadding, y),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f))
                )

                drawContext.canvas.nativeCanvas.drawText(
                    "$value%",
                    yAxisStartPadding - padding / 4,
                    y + 8,
                    Paint().apply {
                        textAlign = Paint.Align.RIGHT
                        textSize = 24f
                        color = onSurface.toArgb()
                    }
                )
            }

            // ---------- TRANSPOSE DATA (CRITICAL) ----------
            val monthWiseData: Map<Month, List<Pair<Int, MonthlyAttendance>>> =
                attendanceData
                    .flatMapIndexed { subjectIndex, subject ->
                        subject.monthlyAttendance.map {
                            it.month to (subjectIndex to it)
                        }
                    }
                    .groupBy(
                        keySelector = { it.first },
                        valueTransform = { it.second }
                    )

            // ---------- DRAW LINES (one per month) ----------
            monthWiseData.forEach { (month, pointsForMonth) ->

                val ordered = pointsForMonth.sortedBy { it.first }

                val points = ordered.map { (subjectIndex, data) ->
                    val p = pointFor(subjectIndex, data.percentage)

                    // draw dot
                    drawCircle(
                        color = data.color,
                        radius = 6f,
                        center = p
                    )

                    // percent line label   (Todo: make it visible only when user clicks
                    drawContext.canvas.nativeCanvas.drawText(
                        "${data.percentage.toInt()}%",
                        p.x,
                        p.y - 10,
                        Paint().apply {
                            textAlign = Paint.Align.CENTER
                            textSize = 22f
                            color = onSurface.toArgb()
                        }
                    )

                    // month name
                    if(data == ordered.last().second){
                        drawContext.canvas.nativeCanvas.drawText(
                            month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            p.x + 30f,
                            p.y + 8f,
                            Paint().apply {
                                textAlign = Paint.Align.LEFT
                                textSize = 22f
                                color = onSurface.toArgb()
                                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                            }
                        )
                    }

                    p
                }

                val smoothPath = buildSmoothPath(points)

                drawPath(
                    path = smoothPath,
                    color = ordered.first().second.color,
                    style = Stroke(
                        width = 4f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // ---------- X axis labels ----------
            // todo: when any subject clicked, show their percentage along with month name
            attendanceData.forEachIndexed { index, subject ->
                val x = pointFor(index).x

                drawLine(
                    color = outline,
                    start = Offset(x, originY - 8),
                    end = Offset(x, originY + 8),
                    strokeWidth = 2f
                )

                drawContext.canvas.nativeCanvas.withRotation(
                    -45f,
                    x,
                    originY
                ) {
                    drawText(
                        subject.subjectName.takeIf { it.length <= 7 } ?: subject.subjectName.getShortForm(4),
                        x,
                        originY + padding,
                        Paint().apply {
                            textAlign = Paint.Align.RIGHT
                            textSize = 22f
                            color = onSurface.toArgb()
                            isAntiAlias = true
                        }
                    )
                }
            }
        }
    }
}

/* -------------------- MODELS -------------------- */

data class SubjectWithMonthlyPercentage(
    val subjectName: String,
    val monthlyAttendance: List<MonthlyAttendance>,
)

data class MonthlyAttendance(
    val month: Month,
    val percentage: Float,
    val color: Color,
)

/* -------------------- DUMMY DATA -------------------- */

fun getDummySubjectPercentages(): List<SubjectWithMonthlyPercentage> {
    val months = listOf(
        Month.JANUARY,
        Month.FEBRUARY,
        Month.MARCH,
        Month.APRIL
    )

    return listOf(
        "Mathematics",
        "Computer Science",
        "Physics",
        "Hindi",
        "Engineering Mathematics",
        "Database Management System",
        "Computer Vision"
    ).map { subject ->
        SubjectWithMonthlyPercentage(
            subjectName = subject,
            monthlyAttendance = months.map { month ->
                MonthlyAttendance(
                    month = month,
                    percentage = (0..100).random().toFloat(),
                    color = ColorUtils.readableColor(false)
                )
            }
        )
    }
}

fun buildSmoothPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path

    path.moveTo(points.first().x, points.first().y)

    for (i in 1 until points.size) {
        val prev = points[i - 1]
        val curr = points[i]

        val smoothFactor = 0.25f
        val dx = curr.x - prev.x

        path.cubicTo(
            prev.x + dx * smoothFactor, prev.y,
            curr.x - dx * smoothFactor, curr.y,
            curr.x, curr.y
        )
    }
    return path
}
