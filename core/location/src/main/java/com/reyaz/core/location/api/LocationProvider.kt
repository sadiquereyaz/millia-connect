package com.reyaz.core.location.api

import com.reyaz.core.location.model.MyLocationResult
import kotlinx.coroutines.flow.Flow

interface LocationProvider {

    /** One-time location fetch */
    suspend fun getCurrentLocation(): MyLocationResult

    /** Continuous updates (optional) */
    fun observeLocation(): Flow<MyLocationResult>
}
