package com.reyaz.core.config

import com.google.firebase.remoteconfig.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext


val configModule = module {

    single {
        FirebaseRemoteConfig.getInstance().apply {
            val settings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(
                    if (BuildConfig.DEBUG) 0 else 3600
                )
                .build()
            setConfigSettingsAsync(settings)
        }
    }

    single<AppVersionProvider> {
        DefaultAppVersionProvider(androidContext())
    }

    single<AppUpdateChecker> {
        FirebaseAppUpdateChecker(
            remoteConfig = get(),
            versionProvider = get()
        )
    }
}