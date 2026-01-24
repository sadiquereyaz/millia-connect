package com.reyaz.feature.attendance.presentation.graph.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

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