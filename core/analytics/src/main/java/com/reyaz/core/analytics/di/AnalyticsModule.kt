package com.reyaz.core.analytics.di

import com.google.firebase.FirebaseApp.getInstance
import com.reyaz.core.analytics.AnalyticsTracker
import com.reyaz.core.analytics.FirebaseAnalyticsTracker
import org.koin.dsl.module

val analyticsModule = module {
    single { getInstance(get()) }
    single<AnalyticsTracker> { FirebaseAnalyticsTracker(get()) }
}
