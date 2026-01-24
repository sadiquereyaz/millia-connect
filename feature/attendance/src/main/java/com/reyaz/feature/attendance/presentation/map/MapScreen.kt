package com.reyaz.feature.attendance.presentation.map

import androidx.compose.runtime.Composable
import com.reyaz.feature.attendance.presentation.map.map_screens.AddClassroomLocationScreen
import com.reyaz.feature.attendance.presentation.map.map_screens.MapplsMap

@Composable
fun MapScreen(
    onConfirm: (Double, Double) -> Unit
) {
    MapplsMap()
//    MapplePinCameraFeatureScreen()
//    MapClickScreen()
    AddClassroomLocationScreen()
//    MapWithSearchBarScreen()
}

