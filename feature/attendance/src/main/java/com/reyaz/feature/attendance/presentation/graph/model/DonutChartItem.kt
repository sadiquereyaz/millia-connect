package com.reyaz.feature.attendance.presentation.graph.model

data class DonutChartItem(
    val label: String,
    val value: Float   // raw value
)

val sampleDonutChartData = listOf(
    DonutChartItem("Item 1", 10f),
    DonutChartItem("Item 2", 4f),
    DonutChartItem("Item 3", 2f)
)
