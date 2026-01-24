package com.reyaz.feature.attendance.presentation.records

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.reyaz.feature.attendance.presentation.graph.components.DonutChart
import com.reyaz.feature.attendance.presentation.graph.components.MultiLineChart
import com.reyaz.feature.attendance.presentation.graph.model.dummyGraphData1
import com.reyaz.feature.attendance.presentation.graph.model.sampleDonutChartData

@Composable
fun AttendanceSummaryScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            MultiLineChart(
                graphData = dummyGraphData1
            )
        }

        item {
            DonutChart(
                data = sampleDonutChartData
            )
        }
    }
}