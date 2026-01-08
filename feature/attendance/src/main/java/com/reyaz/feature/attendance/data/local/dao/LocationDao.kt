package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import com.reyaz.feature.attendance.data.local.model.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(locations: List<LocationEntity>)

    @Query("SELECT * FROM location ORDER BY locationName ASC")
    fun observeLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM location WHERE locationId = :id")
    suspend fun getLocationById(id: Long): LocationEntity?

    @Delete
    suspend fun deleteLocation(location: LocationEntity)
}
