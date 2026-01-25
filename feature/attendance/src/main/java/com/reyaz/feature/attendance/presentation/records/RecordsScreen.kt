package com.reyaz.feature.attendance.presentation.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.core.common.utils.extensions.StringUtils.getShortForm
import com.reyaz.core.common.utils.extensions.StringUtils.toCapSmall
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
            verticalArrangement = Arrangement.spacedBy(32
                .dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            item {
                uiState.multiLineData?.let {
                    MultiLineChart(
                        graphData = it
                    )
                }
            }

            item {
                if (uiState.donutChartData.isNotEmpty()) {
                    LazyRow() {
                        item {
                            Column(
                                Modifier.width(80.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text("")
                                HorizontalDivider(Modifier.fillMaxWidth())
                                Text("Present", Modifier.padding(start = 12.dp))
                                HorizontalDivider(Modifier.fillMaxWidth())
                                Text("Absent", Modifier.padding(start = 12.dp))
                                HorizontalDivider(Modifier.fillMaxWidth())
                                Text("Total", Modifier.padding(start = 12.dp))
                            }
                        }
                        items(uiState.donutChartData) {
                            Row {
                                VerticalDivider(Modifier.height(100.dp))
                                Column(
                                    Modifier.width(60.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(it.label.take(3), Modifier.padding(horizontal = 8.dp))
                                    HorizontalDivider(Modifier.fillMaxWidth())
                                    Text(it.presentCount.toString())
                                    HorizontalDivider(Modifier.fillMaxWidth())
                                    Text("${it.totalCount - it.presentCount}")
                                    HorizontalDivider(Modifier.fillMaxWidth())
                                    Text("${it.totalCount}")
                                }
                            }
                        }
                    }
                }
            }

            item {
                if (uiState.donutChartData.isNotEmpty()) {
                    DonutChart(
                        data = uiState.donutChartData
                    )
                }
            }
        }
        if (isLoading)
            TranslucentLoader(message = "Calculating...")
    }
}