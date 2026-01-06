package com.reyaz.feature.attendance.presentation.map.map_screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.ContextCompat
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.utils.BitmapUtils
import com.mappls.sdk.plugin.annotation.SymbolManager
import com.mappls.sdk.plugin.annotation.SymbolOptions
import com.reyaz.feature.attendance.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

@Composable
fun AddMarkerScreen() {
    // Returns a scope that's cancelled when F is removed from composition
    val coroutineScope = rememberCoroutineScope()

    MapplsMap(
        onSuccess = { mapView, mapplsMap ->
            mapplsMap.getStyle {
                val symbolManager = SymbolManager(mapView, mapplsMap, it)
                val infoWindowSymbolManager = SymbolManager(mapView, mapplsMap, it)
                symbolManager.iconAllowOverlap = true
                infoWindowSymbolManager.iconAllowOverlap = true
                var isEnableInfoWindow = false
                val view = LayoutInflater.from(mapView.context).inflate(R.layout.info_window, null)

                symbolManager.create(
                    SymbolOptions()
                        .icon(
                            BitmapUtils.getBitmapFromDrawable(
                                ContextCompat.getDrawable(
                                    mapView.context,
                                    com.mappls.sdk.maps.R.drawable.mappls_maps_marker_icon_default
                                )
                            )
                        )
                        .position(LatLng(27.0, 77.0))
                )

                mapplsMap.addOnMapClickListener {
                    addMarkerAndMoveCamera(
                        symbolManager,
                        mapView.context,
                        it,
                        mapplsMap
                    )
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
            }
            mapplsMap.cameraPosition = CameraPosition.Builder()
                .target(LatLng(27.0, 77.0))
                .zoom(14.0)
                .build()

        },
        onError = { _, _ ->

        })
}

fun generate(view: View): Bitmap? {
    val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    view.measure(measureSpec, measureSpec)
    val measuredWidth = view.measuredWidth
    val measuredHeight = view.measuredHeight
    view.layout(0, 0, measuredWidth, measuredHeight)
    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    bitmap.eraseColor(Color.TRANSPARENT)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

suspend fun createInfoWindow(infoWindowSymbolManager: SymbolManager, view: View) {
    CoroutineScope(coroutineContext).launch {
        val bitmap = generate(view)
        withContext(Dispatchers.Main) {
            infoWindowSymbolManager.create(
                SymbolOptions().position(LatLng(27.0, 77.0)).iconOffset(
                    arrayOf(-2f, -60f)
                ).icon(bitmap)
            )
        }
    }

}