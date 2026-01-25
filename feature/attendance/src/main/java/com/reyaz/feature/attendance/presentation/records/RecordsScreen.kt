package com.reyaz.feature.attendance.presentation.records

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.core.ui.components.TranslucentLoader
import com.reyaz.feature.attendance.presentation.graph.components.DonutChart
import com.reyaz.feature.attendance.presentation.graph.components.MultiLineChart
import com.reyaz.feature.attendance.presentation.graph.model.dummyGraphData1
import com.reyaz.feature.attendance.presentation.graph.model.sampleDonutChartData
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordsScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            delay(500)
            isLoading = false
        } else {
            isLoading = true
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                uiState.multiLineData?.let {
                    MultiLineChart(
                        graphData = it
                    )
                }
            }

            item {
                DonutChart(
                    data = sampleDonutChartData
                )
            }
        }
        if (isLoading)
            TranslucentLoader(message = "Calculating...")
    }
}