package com.reyaz.feature.attendance.presentation.map


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.mappls.sdk.maps.MapView
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.OnMapReadyCallback
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
import timber.log.Timber
import kotlin.coroutines.coroutineContext


@Composable
fun MapplsMapView(
    modifier: Modifier = Modifier,
    onLocationPicked: (Double, Double) -> Unit
) {
    val context = LocalContext.current

    val mapView = remember { MapView(context) }

    MapViewLifecycle(mapView)
    // Returns a scope that's cancelled when F is removed from composition
    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = {
            mapView.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(mapplsMap: MapplsMap) {
                    mapplsMap.getStyle { style ->
                        val symbolManager = SymbolManager(mapView, mapplsMap, style)
                        val infoWindowSymbolManager = SymbolManager(mapView, mapplsMap, style)
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
//                                            com.reyaz.core.ui.R.drawable.ic_launcher_foreground
                                            com.mappls.sdk.maps.R.drawable.mappls_maps_marker_icon_default
                                        )
                                    )
                                )
                                .position(LatLng(27.0, 77.0))
                        )

                        mapplsMap.addOnMapClickListener {
                            symbolManager.create(
                                SymbolOptions()
                                    .icon(
                                        BitmapUtils.getBitmapFromDrawable(
                                            ContextCompat.getDrawable(
                                                mapView.context,
                                            com.reyaz.core.ui.R.drawable.ic_launcher_foreground
//                                                com.mappls.sdk.maps.R.drawable.mappls_maps_marker_icon_default
                                            )
                                        )
                                    )
                                    .position(LatLng(it.latitude, it.longitude))
                            )
                            if(isEnableInfoWindow) {
                                isEnableInfoWindow = false
                                infoWindowSymbolManager.clearAll()

                                return@addOnMapClickListener true
                            }
                            return@addOnMapClickListener false
                        }

                        symbolManager.addClickListener {
                            if(!isEnableInfoWindow) {
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

                }

                override fun onMapError(code: Int, message: String?) {
                    Timber.e("Map error: $code, $message")
                }
            })
        }
    )
}

suspend fun createInfoWindow(infoWindowSymbolManager: SymbolManager, view: View) {
    CoroutineScope(coroutineContext).launch {
        val bitmap = generate(view)
        withContext(Dispatchers.Main) {
            infoWindowSymbolManager.create(SymbolOptions().position(LatLng(27.0, 77.0)).iconOffset(
                arrayOf(-2f, -60f)).icon(bitmap))
        }
    }

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