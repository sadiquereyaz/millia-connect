package com.reyaz.feature.attendance.di

import androidx.room.Room
import com.reyaz.feature.attendance.data.local.AttendanceDatabase
import com.reyaz.feature.attendance.data.repository.AttendanceRepositoryImpl
import com.reyaz.feature.attendance.data.repository.LectureRepositoryImpl
import com.reyaz.feature.attendance.data.repository.LocationRepositoryImpl
import com.reyaz.feature.attendance.domain.repo.AttendanceRepository
import com.reyaz.feature.attendance.presentation.add_schedule.UpdateScheduleViewModel
import com.reyaz.feature.attendance.presentation.schedule.ScheduleViewModel
import com.reyaz.feature.attendance.domain.repo.LectureRepository
import com.reyaz.feature.attendance.domain.repo.LocationRepository
import com.reyaz.feature.attendance.domain.repo.ScheduleRepository
import com.reyaz.feature.attendance.domain.usecase.GetAttendanceGraphUseCase
import com.reyaz.feature.attendance.presentation.records.RecordsViewModel
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
    single { get<AttendanceDatabase>().scheduleDao() }
    single { get<AttendanceDatabase>().attendanceDao() }

    // Repository
    single<LectureRepository> {
        LectureRepositoryImpl(get(), get())
    }
    single<AttendanceRepository> {
        AttendanceRepositoryImpl(get(), get())
    }
    single<LocationRepository> {
        LocationRepositoryImpl(get())
    }

    // usecase
    single { GetAttendanceGraphUseCase(get()) }

    // ViewModels
    viewModel {
        ScheduleViewModel(get(), get())
    }
    viewModel {
        UpdateScheduleViewModel(get(), get(),get())
    }
    viewModel {
        RecordsViewModel(get())
    }
}
