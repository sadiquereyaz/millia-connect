package com.reyaz.feature.attendance.utils.mapper

import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.domain.model.LocationModel

fun LocationEntity.toLocationDomain(): LocationModel {
    return LocationModel(
        locationId = this.locationId,
        locationName = this.locationName,
        latitude = this.latitude,
        longitude = this.longitude
    )
}