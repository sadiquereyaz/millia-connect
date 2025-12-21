package com.reyaz.feature.attendance.di


import com.reyaz.feature.attendance.add_schedule.presentation.AddScheduleViewModel
import com.reyaz.feature.attendance.presentation.schedule.ScheduleViewModel
import com.reyaz.feature.attendance.schedule.presentation.ScheduleViewModelOld
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val attendanceModule = module {
    viewModel {
        AddScheduleViewModel(get())
    }
    viewModel {
        ScheduleViewModel()
    }
    viewModel {
        ScheduleViewModelOld()
    }
}
