package com.reyaz.feature.attendance.presentation.graph.model

import kotlinx.datetime.Month

data class GraphData(
    val subjects: List<String>,            // x-axis
    val lineData: List<LineData>,          // y
) {
    val isValid: Boolean = lineData.isNotEmpty() && lineData.all {
        it.percentages.size == subjects.size
    }
}

data class LineData(
    val month: Month,
    val percentages: List<Float>,
)

/* -------------------- DUMMY DATA -------------------- */

val dummyGraphData1 = GraphData(
    subjects = listOf("Kotlin", "Jetpack Compose", "Coroutines"),
    lineData = listOf(
        LineData(month = Month(1), percentages = listOf(85f, 70f, 60f)),
        LineData(month = Month(2), percentages = listOf(90f, 82f, 45f)),
        LineData(month = Month(3), percentages = listOf(95f, 88f, 55f)),
    )
)
