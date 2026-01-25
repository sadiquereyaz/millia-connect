package com.reyaz.feature.attendance.domain.model

data class LocationModel(
    val locationId: Long,
    val locationName: String,
    val latitude: Double,
    val longitude: Double
)