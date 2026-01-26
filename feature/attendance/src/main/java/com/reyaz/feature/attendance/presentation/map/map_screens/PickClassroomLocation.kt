package com.reyaz.feature.attendance.presentation.map.map_screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.mappls.sdk.geojson.Point
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mappls.sdk.plugins.places.placepicker.PlacePicker
import com.mappls.sdk.plugins.places.placepicker.model.PlacePickerOptions
import com.mappls.sdk.services.api.Place
import com.reyaz.feature.attendance.utils.toDetailedString
import timber.log.Timber

@Composable
fun PickClassroomLocation(
    onPlacePicked: (Place) -> Unit
) {
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(result.data!!)
                Timber.d(place?.toDetailedString() ?: "Place is null")
                place?.let(onPlacePicked)
            }
        }

    Button(onClick = {
        // Target coordinates for search
        val targetLat = 55.0
        val targetLng = 98.0

        // Configure search to be location-based (searches near the specified coordinates)
        val searchPlaceOptions = PlaceOptions.builder()
            .location(Point.fromLngLat(targetLng, targetLat))
            .build()

        val intent = PlacePicker.IntentBuilder()
            .placeOptions(
                PlacePickerOptions.builder()
                    .statingCameraPosition(
                        CameraPosition.Builder()
                            .target(LatLng(targetLat, targetLng))
                            .zoom(14.0)
                            .build()
                    )
                    .searchPlaceOption(searchPlaceOptions)
                    .includeSearch(true)
                    .includeDeviceLocationButton(true)
                    .build()
            )
            .build(context as Activity?)

        launcher.launch(intent)
    }) {
        Text("Pick Classroom Location")
    }
}
