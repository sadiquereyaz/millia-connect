package com.reyaz.feature.attendance.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MapPickerScreen(
    onConfirm: (Double, Double) -> Unit
) {
    MapplsMapView(
        modifier = Modifier.fillMaxSize(),
        onLocationPicked = { lat, lng ->
            onConfirm(lat, lng)
        }
    )
}
