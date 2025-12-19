package com.reyaz.core.location.di

import com.reyaz.core.location.api.LocationProvider
import com.reyaz.core.location.impl.FusedLocationProviderImpl
import org.koin.dsl.module

val locationModule = module {
    single<LocationProvider> {
        FusedLocationProviderImpl(get())
    }
}
