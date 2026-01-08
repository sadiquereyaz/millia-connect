package com.reyaz.feature.attendance.presentation.map.map_screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mappls.sdk.maps.camera.CameraPosition.*
import com.mappls.sdk.services.api.Place
import com.reyaz.feature.attendance.presentation.map.components.PlaceDetailsSheet

@Composable
fun AddClassroomLocationScreen() {
    var selectedPlace by remember { mutableStateOf<Place?>(null) }

    PickClassroomLocation { place ->
        selectedPlace = place
    }

    selectedPlace?.let { place ->
        PlaceDetailsSheet(
            place = place,
            onConfirm = {
                // Convert to domain model
                // save to DB
            }
        )
    }
}
