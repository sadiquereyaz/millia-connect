package com.reyaz.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.reyaz.feature.attendance.data.local.model.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Delete
    suspend fun deleteLocation(location: LocationEntity)

    @Query("DELETE FROM location WHERE locationId = :locationId")
    suspend fun deleteLocation(locationId: Long)

    @Query("SELECT * FROM location ORDER BY locationName ASC")
    fun observeLocations(): Flow<List<LocationEntity>>

    @Upsert
    suspend fun upsertLocation(location: LocationEntity): Long
}
