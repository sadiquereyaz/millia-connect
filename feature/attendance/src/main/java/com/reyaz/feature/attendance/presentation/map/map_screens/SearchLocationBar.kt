package com.reyaz.feature.attendance.presentation.map.map_screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.camera.CameraPosition.*
import com.mappls.sdk.maps.camera.CameraUpdateFactory
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mappls.sdk.plugins.places.autocomplete.model.PlaceOptions
import com.mappls.sdk.services.api.autosuggest.model.ELocation
import com.reyaz.core.ui.components.SingleLineText

@Composable
fun SearchLocationBar(
    modifier: Modifier = Modifier,
    selectedLocationName: String? = null,
    onLocationSelected: (ELocation) -> Unit
) {
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val location = PlaceAutocomplete.getPlace(result.data!!)
                if (location != null) {
                    onLocationSelected(location)
                }
            }
        }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable {
                val placeOptions = PlaceOptions.builder()
//            .location()
//            .zoom()
//            .currentLocationIcon()
                    .build(PlaceOptions.MODE_FULLSCREEN)

                val intent = PlaceAutocomplete.IntentBuilder()
                    .placeOptions(placeOptions)
                    .build(context as Activity?)

                launcher.launch(intent)
            }
            .background(MaterialTheme.colorScheme.background.copy(alpha = if (selectedLocationName.isNullOrBlank()) 0.4f else 0.8f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp, 12.dp)
        ) {
            Icon(Icons.Default.Search, "search")
            Spacer(Modifier.width(16.dp))
            SingleLineText(
                text = selectedLocationName ?: "Search",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2
            )
        }
    }
}

fun moveCamera(
    mapplsMap: MapplsMap,
    location: LatLng
) {
    val latLng = location

    val cameraPosition = Builder()
        .target(latLng)
        .zoom(16.0)
        .build()

    mapplsMap.animateCamera(
        CameraUpdateFactory.newCameraPosition(cameraPosition)
    )
}
