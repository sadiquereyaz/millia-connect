package com.reyaz.milliaconnect1.di

import androidx.work.WorkManager
import com.reyaz.core.common.env.AppEnvironment
import com.reyaz.core.config.AppViewModel
import com.reyaz.milliaconnect1.util.AppEnvironmentImpl
import com.reyaz.milliaconnect1.util.NetworkConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { NetworkConnectivityObserver(get()) }
    single { WorkManager.getInstance(androidContext()) }
    viewModel { AppViewModel(get()) }
    single<AppEnvironment> {
        AppEnvironmentImpl()
    }
}