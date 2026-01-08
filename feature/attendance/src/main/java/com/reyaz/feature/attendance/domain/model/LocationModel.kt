package com.reyaz.feature.attendance.domain.model

data class LocationModel(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

val dummyLocations = listOf(
    LocationModel(
        id = 1L,
        name = "Headquarters",
        latitude = 28.6139,
        longitude = 77.2090
    ),
    LocationModel(
        id = 2L,
        name = "Regional Office - Mumbai",
        latitude = 19.0760,
        longitude = 72.8777
    ),
    LocationModel(
        id = 3L,
        name = "Tech Park - Bangalore",
        latitude = 12.9716,
        longitude = 77.5946
    ),
    LocationModel(
        id = 4L,
        name = "Branch Office - Kolkata",
        latitude = 22.5726,
        longitude = 88.3639
    ),
    LocationModel(
        id = 5L,
        name = "Support Center - Hyderabad",
        latitude = 17.3850,
        longitude = 78.4867
    )
)
