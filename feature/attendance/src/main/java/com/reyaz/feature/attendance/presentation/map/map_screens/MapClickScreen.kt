package com.reyaz.feature.attendance.presentation.map.map_screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.mappls.sdk.maps.camera.CameraPosition.*
import com.mappls.sdk.maps.geometry.LatLng

@Composable
fun MapClickScreen() {
    val context = LocalContext.current
    MapplsMap(onSuccess = { mapView, mapplsMap ->
        mapplsMap.cameraPosition = Builder()
            .target(LatLng(27.0, 78.0))
            .zoom(14.0)
            .build()
        mapplsMap.addOnMapClickListener { latlng ->
            Toast.makeText(
                context,
                "${latlng.latitude}, ${latlng.longitude}",
                Toast.LENGTH_SHORT
            ).show()
            return@addOnMapClickListener false
        }
    })
}