package com.reyaz.feature.attendance.presentation.map

import androidx.compose.runtime.Composable
import com.reyaz.feature.attendance.presentation.map.map_screens.AddClassroomLocationScreen
import com.reyaz.feature.attendance.presentation.map.map_screens.MapWithSearchBarScreen
import com.reyaz.feature.attendance.presentation.map.map_screens.MapplsMap
import com.reyaz.feature.attendance.presentation.map.map_screens.SearchLocationBar

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
