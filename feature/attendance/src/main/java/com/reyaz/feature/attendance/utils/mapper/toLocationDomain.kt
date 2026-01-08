package com.reyaz.feature.attendance.utils.mapper

import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.domain.model.LocationModel

fun LocationEntity.toLocationDomain(): LocationModel {
    return LocationModel(
        id = this.locationId,
        name = this.locationName,
        latitude = this.latitude,
        longitude = this.longitude
    )
}