package com.reyaz.feature.attendance.data.repository

import com.reyaz.feature.attendance.data.local.dao.AttendanceDao
import com.reyaz.feature.attendance.data.local.dao.LocationDao
import com.reyaz.feature.attendance.data.local.dao.ScheduleDao
import com.reyaz.feature.attendance.data.local.dao.SubjectDao
import com.reyaz.feature.attendance.data.local.model.AttendanceEntity
import com.reyaz.feature.attendance.data.local.model.LectureEntity
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import com.reyaz.feature.attendance.domain.model.AttendanceStatus
import com.reyaz.feature.attendance.domain.model.LocationModel
import com.reyaz.feature.attendance.domain.model.ScheduleLectureUiModel
import com.reyaz.feature.attendance.domain.repo.AttendanceRepository
import com.reyaz.feature.attendance.domain.repo.LectureRepository
import com.reyaz.feature.attendance.domain.repo.LocationRepository
import com.reyaz.feature.attendance.utils.mapper.toLocationDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import timber.log.Timber

class LocationRepositoryImpl(
    private val locationDao: LocationDao,
) : LocationRepository {
    override suspend fun insertLocation(location: LocationEntity): Long {
        return locationDao.upsertLocation(location)
    }

    override fun observeAllLocations(): Flow<List<LocationModel>> {
        return locationDao.observeLocations().map { entities ->
            entities.map { it.toLocationDomain() }
        }
    }
}
