package com.reyaz.feature.attendance.add_schedule.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddAttendanceScreen(
    viewModel: AddScheduleViewModel,
) {
    val context = LocalContext.current
    // Define the list of permissions you want to request.
    // BACKGROUND_LOCATION needs to be requested separately after FINE_LOCATION is granted.
    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Create a launcher for multiple permissions.
    // The result is a map of permission names to their granted status (Boolean).
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions: Map<String, Boolean> ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            // Location access granted. Now you can request background location if needed,
            // or proceed with location-based features.
            // viewModel.onLocationPermissionGranted()
        } else {
            // Location access denied. Handle this case gracefully.
            // viewModel.onLocationPermissionDenied()
        }
    }

    // Use LaunchedEffect to check and request permissions when the screen is first composed.
    LaunchedEffect(Unit) {
        val permissionStatus = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            // Request the permission
            permissionLauncher.launch(permissionsToRequest)
        } else {
            viewModel.getCurrentLocation()
        }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Adding Attendance Schedule...")
        Text("Current Location Coordinates: ${uiState.currentLocation}")
        uiState.message?.let {
            Text(it)
        }
    }
}
