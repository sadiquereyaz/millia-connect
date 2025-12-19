package com.reyaz.core.location.impl

import com.reyaz.core.location.api.LocationProvider
import com.reyaz.core.location.model.MyLocationResult
import kotlinx.coroutines.flow.flowOf

class FakeLocationProvider : LocationProvider {

    override suspend fun getCurrentLocation(): MyLocationResult =
        MyLocationResult.Success(
            latitude = 28.5616,
            longitude = 77.2802
        )

    override fun observeLocation() = flowOf(
        MyLocationResult.Success(28.5616, 77.2802)
    )
}
