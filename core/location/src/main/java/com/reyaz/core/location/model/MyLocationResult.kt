package com.reyaz.core.location.model

sealed class MyLocationResult {
    data class Success(
        val latitude: Double,
        val longitude: Double,
        val accuracy: Float? = null
    ) : MyLocationResult()

    object PermissionDenied : MyLocationResult()
    object LocationDisabled : MyLocationResult()
    object Timeout : MyLocationResult()
    data class Error(val reason: String) : MyLocationResult()
}
