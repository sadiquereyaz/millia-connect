package com.reyaz.feature.attendance.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Long = 0L,
    val name: String, // todo: rename to locationName
)
