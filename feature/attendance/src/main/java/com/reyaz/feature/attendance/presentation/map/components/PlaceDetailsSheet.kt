package com.reyaz.feature.attendance.presentation.map.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mappls.sdk.maps.camera.CameraPosition.*
import com.mappls.sdk.services.api.Place

@Composable
fun PlaceDetailsSheet(
    place: Place,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = place.houseName ?: "Unknown place",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        Text(text = place.formattedAddress ?: "")

        Spacer(Modifier.height(12.dp))

        Text(text = "Latitude: ${place.lat}")
        Text(text = "Longitude: ${place.lng}")

        Spacer(Modifier.height(16.dp))

        Button(onClick = onConfirm) {
            Text("Use this location")
        }
    }
}
