package com.reyaz.feature.attendance.presentation.records

import com.reyaz.feature.attendance.presentation.graph.model.DonutChartItem
import com.reyaz.feature.attendance.presentation.graph.model.GraphData

data class RecordsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val multiLineData: GraphData? = null,
    val donutChartData: List<DonutChartItem> = emptyList()
)
