package com.reyaz.feature.attendance.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Long = 0L,
    val subjectName: String,
)
