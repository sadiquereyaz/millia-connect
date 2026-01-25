package com.reyaz.feature.attendance.presentation.schedule

import com.reyaz.feature.attendance.domain.model.ScheduleLectureUiModel
import com.reyaz.feature.attendance.utils.TimeUtils
import kotlinx.datetime.LocalDate


data class ScheduleUiState(
    val isLoading: Boolean = false,
    val selectedDate: LocalDate = TimeUtils.today(),
    val overAllPer: Int? = null,
    val targetPer: Int? = null,
    val lectureSlots: List<ScheduleLectureUiModel> = emptyList(),
)