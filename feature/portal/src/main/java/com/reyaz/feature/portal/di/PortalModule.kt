package com.reyaz.feature.portal.di

import com.reyaz.core.common.env.AppEnvironment
import com.reyaz.core.common.utils.FakeNetworkManager
import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.core.common.utils.NetworkManagerImpl
import com.reyaz.feature.portal.data.local.PortalDataStore
import com.reyaz.feature.portal.data.remote.PortalScraper
import com.reyaz.feature.portal.data.repository.MockPortalRepoImpl
import com.reyaz.feature.portal.data.repository.PortalRepositoryImpl
import com.reyaz.feature.portal.domain.repository.FirestorePromoRepository
import com.reyaz.feature.portal.domain.repository.PortalRepository
import com.reyaz.feature.portal.presentation.PortalViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val portalModule = module {

    single<NetworkManager> {
        val env: AppEnvironment = get()
        if (env.isDebug) FakeNetworkManager()
        else NetworkManagerImpl(get())
    }

    single { PortalDataStore(get()) }
    single { PortalScraper(get<NetworkManager>(), get()) }
    viewModel { PortalViewModel(get(), get(), get(), get(), get()) }
    single<FirestorePromoRepository> { FirestorePromoRepository(get()) }

    single<PortalRepository> {
        val env: AppEnvironment = get()

        if (env.isDebug) {
            MockPortalRepoImpl(get(), get(), get())
        } else {
            PortalRepositoryImpl(get(), get(), get(), get(), get())
        }
    }
}