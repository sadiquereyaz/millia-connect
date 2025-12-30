package com.reyaz.feature.attendance.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val locationId: Long = 0L,
    val coordinates: String,
    val locationName: String,
)
