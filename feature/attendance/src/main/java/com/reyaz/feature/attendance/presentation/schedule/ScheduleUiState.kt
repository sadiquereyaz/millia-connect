package com.reyaz.feature.attendance.presentation.schedule

import android.text.style.TtsSpan
import com.reyaz.feature.attendance.data.local.model.LectureAttendanceWithSubject
import com.reyaz.feature.attendance.utils.TimeUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn


data class ScheduleUiState(
    val isLoading: Boolean = false,
    val selectedDate: LocalDate = TimeUtils.today(),
    val overAllPer: Int? = null,
    val targetPer: Int? = null,
    val lectures: List<LectureAttendanceWithSubject> = emptyList(),
)