package com.reyaz.feature.attendance.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val locationId: Long = 0L,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
)
