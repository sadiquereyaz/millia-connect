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
import com.mappls.sdk.services.api.OnResponseCallback
import com.mappls.sdk.services.api.PlaceResponse
import com.mappls.sdk.services.api.autosuggest.model.ELocation
import com.mappls.sdk.services.api.reversegeocode.MapplsReverseGeoCode
import com.mappls.sdk.services.api.reversegeocode.MapplsReverseGeoCodeManager
import com.reyaz.feature.attendance.R
import com.reyaz.feature.attendance.utils.toDetailedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MapWithSearchBarScreen() {
    // Returns a scope that's cancelled when F is removed from composition
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // State to hold the selected location name
    var selectedLocationName by remember { mutableStateOf<String?>(null) }

    // References to map and symbol manager
    var mapplsMapRef: MapplsMap? by remember { mutableStateOf(null) }
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

                    mapplsMap.addOnMapClickListener { clickedLatLng ->
                        // Perform reverse geocoding to get place details
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                val reverseGeoCode = MapplsReverseGeoCode.builder()
                                    .setLocation(clickedLatLng.latitude, clickedLatLng.longitude)
                                    .build()

                                MapplsReverseGeoCodeManager.newInstance(reverseGeoCode).call(
                                    object : OnResponseCallback<PlaceResponse> {
                                        override fun onSuccess(response: PlaceResponse?) {
                                            response?.let { placeResponse ->
                                                    placeResponse.places?.forEach { place ->
                                                        Timber.tag("MAPPLS_LOG").d(place.toDetailedString())
                                                    }
                                                    // Update the selected location name on the main thread
                                                    coroutineScope.launch(Dispatchers.Main) {
                                                        selectedLocationName = "${placeResponse.places.firstOrNull()?.formattedAddress}"
                                                        // todo: no need to move the camera here, just add the marker
                                                        addMarkerAndMoveCamera(
                                                            symbolManagerRef,
                                                            context,
                                                            clickedLatLng,
                                                            mapplsMapRef
                                                        )
                                                    }
                                                }
                                            }

                                            override fun onError(p0: Int, p1: String?) {
                                                Timber.e(p1)
                                                // Fallback: just add marker without name
                                                coroutineScope.launch(Dispatchers.Main) {
                                                    selectedLocationName = String.format(
                                                        java.util.Locale.getDefault(),
                                                        "%.4f, %.4f",
                                                        clickedLatLng.latitude,
                                                        clickedLatLng.longitude
                                                    )
                                                    // todo: no need to move the camera here, just add the marker
                                                    addMarkerAndMoveCamera(
                                                        symbolManagerRef,
                                                        context,
                                                        clickedLatLng,
                                                        mapplsMapRef
                                                    )
                                                }
                                            }
                                        }
                                        )
                                    } catch (e: Exception) {
                                    Timber.e(e)
                                    // Fallback: just add marker without name
                                    coroutineScope.launch(Dispatchers.Main) {
                                        selectedLocationName = String.format(
                                            java.util.Locale.getDefault(),
                                            "%.4f, %.4f",
                                            clickedLatLng.latitude,
                                            clickedLatLng.longitude
                                        )
                                        addMarkerAndMoveCamera(
                                            symbolManagerRef,
                                            context,
                                            clickedLatLng,
                                            mapplsMapRef
                                        )
                                    }
                                }
                            }

                            if (isEnableInfoWindow) {
                                isEnableInfoWindow = false
                                infoWindowSymbolManager.clearAll()
                                return@addOnMapClickListener true
                            }
                            return@addOnMapClickListener false
                        }

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
                        addMarkerAndMoveCamera(
                            symbolManagerRef,
                            context,
                            selectedLatLng,
                            mapplsMapRef
                        )
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