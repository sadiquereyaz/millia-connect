package com.reyaz.feature.attendance.presentation.map.map_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.camera.CameraMapplsPinUpdateFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapplePinCameraFeatureScreen() {
    var mapplsMap1: MapplsMap? = null
    val context = LocalContext.current

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Mappls Pin Camera Features") })
    }) { it ->

        Column(
            Modifier.padding(it)
        ) {

            MapplsMap(
                modifier = Modifier.weight(1f),
                onSuccess = { mapView, mapplsMap ->
                    mapplsMap1 = mapplsMap
                    mapplsMap.setPadding(20, 20, 20, 20)

                    mapplsMap.animateCamera(
                        CameraMapplsPinUpdateFactory.newMapplsPinZoom(
                            "MMI000",
                            14.0
                        )
                    )
                },

                onError = { _, _ ->

                })


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Move Camera", color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .clickable(enabled = true) {
                            mapplsMap1?.moveCamera(
                                CameraMapplsPinUpdateFactory.newMapplsPinZoom(
                                    "2T7S17",
                                    14.0
                                )
                            )
                        },
                )
                Text(
                    text = "Ease Camera", color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .clickable(enabled = true) {
                            mapplsMap1?.easeCamera(
                                CameraMapplsPinUpdateFactory.newMapplsPinZoom(
                                    "5EU4EZ",
                                    14.0
                                )
                            )
                        },
                )
                Text(
                    text = "Animate Camera", color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .clickable(enabled = true) {
                            mapplsMap1?.animateCamera(
                                CameraMapplsPinUpdateFactory.newMapplsPinZoom(
                                    "IB3BR9",
                                    14.0
                                )
                            )
                        },
                )
            }


        }
    }


}

