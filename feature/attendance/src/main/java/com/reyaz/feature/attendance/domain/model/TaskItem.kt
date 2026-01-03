package com.reyaz.feature.attendance.domain.model

import kotlinx.datetime.LocalDateTime

data class TaskItem(
    val id: Int,
    val task: String,
    val reminder: LocalDateTime
)