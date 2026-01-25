package com.reyaz.feature.attendance.presentation.graph.model

data class DonutChartItem(
    val label: String,
    val presentCount: Int,
    val totalCount: Int
) {
    val percentage: Float
        get() = if (totalCount == 0) 0f else (presentCount * 100f) / totalCount
}

val sampleDonutChartData = listOf(
    DonutChartItem("Item 1", presentCount = 8, totalCount = 10),
    DonutChartItem("Item 2", presentCount = 3, totalCount = 4),
    DonutChartItem("Item 3", presentCount = 2, totalCount = 2)
)
