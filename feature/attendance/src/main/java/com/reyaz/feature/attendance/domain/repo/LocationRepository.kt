package com.reyaz.feature.attendance.domain.repo

import com.reyaz.feature.attendance.data.local.model.LectureEntity
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.domain.model.ScheduleLectureUiModel
import com.reyaz.feature.attendance.domain.model.LocationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

interface LocationRepository {
    suspend fun insertLocation(location: LocationEntity): Long
    fun observeAllLocations(): Flow<List<LocationModel>>
}
