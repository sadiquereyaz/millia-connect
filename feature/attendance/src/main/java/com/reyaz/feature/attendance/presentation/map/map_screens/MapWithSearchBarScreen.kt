package com.reyaz.feature.attendance.presentation.map.map_screens

import android.content.Context
import android.view.LayoutInflater
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.utils.BitmapUtils
import com.mappls.sdk.plugin.annotation.SymbolManager
import com.mappls.sdk.plugin.annotation.SymbolOptions
import com.mappls.sdk.services.api.autosuggest.model.ELocation
import com.mappls.sdk.services.api.reversegeocode.MapplsReverseGeoCode
import com.mappls.sdk.services.api.reversegeocode.MapplsReverseGeoCodeManager
import com.reyaz.feature.attendance.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MapWithSearchBarScreen() {
    // Returns a scope that's cancelled when F is removed from composition
    val coroutineScope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current

    // State to hold the selected location name
    var selectedLocationName by remember { mutableStateOf<String?>(null) }

    // References to map and symbol manager
    var mapplsMapRef: com.mappls.sdk.maps.MapplsMap? by remember { mutableStateOf(null) }
    var symbolManagerRef: SymbolManager? by remember { mutableStateOf(null) }
    var selectedELocation: ELocation? by remember { mutableStateOf(null) }
    var currentLocation: LatLng? by remember { mutableStateOf(LatLng(28.5605, 77.2836)) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MapplsMap(
            onSuccess = { mapView, mapplsMap ->
                mapplsMapRef = mapplsMap
                mapplsMap.getStyle {
                    val symbolManager = SymbolManager(mapView, mapplsMap, it)
                    symbolManagerRef = symbolManager
                    val infoWindowSymbolManager = SymbolManager(mapView, mapplsMap, it)
                    symbolManager.iconAllowOverlap = true
                    infoWindowSymbolManager.iconAllowOverlap = true
                    var isEnableInfoWindow = false
                    val view =
                        LayoutInflater.from(mapView.context).inflate(R.layout.info_window, null)

                    /*

                    mapplsMap.addOnMapClickListener {
                        if (isEnableInfoWindow) {
                            isEnableInfoWindow = false
                            infoWindowSymbolManager.clearAll()

                            return@addOnMapClickListener true
                        }
                        return@addOnMapClickListener false
                    }*/

                    symbolManager.addClickListener {
                        if (!isEnableInfoWindow) {
                            coroutineScope.launch(Dispatchers.IO) {
                                createInfoWindow(infoWindowSymbolManager, view)
                            }
                            isEnableInfoWindow = true

                            return@addClickListener true
                        }
                        return@addClickListener false
                    }
                    if (currentLocation != null) {
                        symbolManager.create(
                            SymbolOptions()
                                .icon(
                                    BitmapUtils.getBitmapFromDrawable(
                                        ContextCompat.getDrawable(
                                            mapView.context,
                                            com.mappls.sdk.maps.R.drawable.mappls_maps_mylocation_icon_bearing
                                        )
                                    )
                                )
                                .position(currentLocation)
                        )
                        if (selectedELocation == null) {
                            mapplsMap.cameraPosition = CameraPosition.Builder()
                                .target(currentLocation)
                                .zoom(16.0)
                                .build()
                        }
                    }
                }

            },
            onError = { _, _ ->

            }
        )
        SearchLocationBar(
            modifier = Modifier.padding(16.dp),
            selectedLocationName = selectedLocationName,
            onLocationSelected = { location ->
                // Update the selected location name
                selectedELocation = location
                selectedLocationName = location.placeName ?: location.placeAddress

                // Get the latitude and longitude from the selected location
                val latitude = location.latitude ?: 0.0
                val longitude = location.longitude ?: 0.0
                val selectedLatLng = LatLng(latitude, longitude)
                addMarkerAndMoveCamera(symbolManagerRef, context, selectedLatLng, mapplsMapRef)
            }
        )

    }
}

fun addMarkerAndMoveCamera(
    symbolManagerRef: SymbolManager?,
    context: Context,
    selectedLatLng: LatLng,
    mapplsMapRef: MapplsMap?
) {
    // Clear previous markers and add a new marker at the selected location
    symbolManagerRef?.let { manager ->
        manager.clearAll()
        manager.create(
            SymbolOptions()
                .icon(
                    BitmapUtils.getBitmapFromDrawable(
                        ContextCompat.getDrawable(
                            context,
                            com.mappls.sdk.maps.R.drawable.mappls_maps_marker_icon_default
                        )
                    )
                )
                .position(selectedLatLng)
        )
    }

    // Move the camera to the selected location
    mapplsMapRef?.let { map ->
        moveCamera(map, selectedLatLng)
    }
}