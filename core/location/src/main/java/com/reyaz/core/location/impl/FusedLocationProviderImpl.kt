package com.reyaz.core.location.impl

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.reyaz.core.location.api.LocationProvider
import com.reyaz.core.location.model.MyLocationResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FusedLocationProviderImpl(
    context: Context
) : LocationProvider {

    private val fusedClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): MyLocationResult =
        suspendCancellableCoroutine { cont ->

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                cont.resume(MyLocationResult.LocationDisabled)
                return@suspendCancellableCoroutine
            }

            fusedClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        cont.resume(
                            MyLocationResult.Success(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                accuracy = location.accuracy
                            )
                        )
                    } else {
                        cont.resume(MyLocationResult.Timeout)
                    }
                }
                .addOnFailureListener {
                    cont.resume(MyLocationResult.Error(it.message ?: "Unknown error"))
                }
        }

    override fun observeLocation() = TODO("Optional")
}
