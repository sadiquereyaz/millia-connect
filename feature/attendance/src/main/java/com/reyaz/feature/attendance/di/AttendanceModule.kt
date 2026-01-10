package com.reyaz.feature.attendance.di

import androidx.room.Room
import com.reyaz.feature.attendance.data.local.AttendanceDatabase
import com.reyaz.feature.attendance.data.repository.ScheduleRepositoryImpl
import com.reyaz.feature.attendance.presentation.add_schedule.UpdateScheduleViewModel
import com.reyaz.feature.attendance.presentation.schedule.ScheduleViewModel
import com.reyaz.feature.attendance.domain.repo.ScheduleRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val attendanceModule = module {
    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AttendanceDatabase::class.java,
            AttendanceDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    // DAOs
    single { get<AttendanceDatabase>().subjectDao() }
    single { get<AttendanceDatabase>().locationDao() }
    single { get<AttendanceDatabase>().lectureSlotDao() }
    single { get<AttendanceDatabase>().attendanceDao() }
    single { get<AttendanceDatabase>().scheduleDao() }
    single { get<AttendanceDatabase>().attendanceSummaryDao() }

    // Repository
    single<ScheduleRepository> {
        ScheduleRepositoryImpl(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    // ViewModels
    viewModel {
        ScheduleViewModel(get())
    }
    viewModel {
        UpdateScheduleViewModel(get(), get())
    }
}
