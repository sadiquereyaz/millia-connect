package com.reyaz.core.common.utils

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class FakeNetworkManager : NetworkManager {

    private val wifi = MutableStateFlow(true)
    private val mobile = MutableStateFlow(false)
    private val internet = MutableStateFlow(true)
    private val captivePortal = MutableStateFlow(true)
    private val vpn = MutableStateFlow(false)

    // ---- setters (only for debug/testing) ----

    fun setWifiConnected(value: Boolean) {
        wifi.value = value
    }

    fun setMobileDataConnected(value: Boolean) {
        mobile.value = value
    }

    fun setInternetAvailable(value: Boolean) {
        internet.value = value
    }

    fun setCaptivePortalPresent(value: Boolean) {
        captivePortal.value = value
    }

    fun setVpnActive(value: Boolean) {
        vpn.value = value
    }

    // ---- NetworkManager impl ----

    override fun setCaptivePortal(intent: Intent?) = Unit

    override fun reportCaptivePortalDismissed() {
        captivePortal.value = false
    }

    override fun bindToWifiNetwork() = Unit

    override fun resetNetworkBinding() = Unit

    override fun observeWifiConnectivity(): Flow<Boolean> = wifi

    override fun observeMobileDataConnectivity(): Flow<Boolean> = mobile

    override fun observeCaptivePortalConnectivity(): Flow<Boolean> = captivePortal

    override fun observeInternetConnectivity(): Flow<Boolean> = internet

    override fun observeAllNetworkType(): Flow<NetworkPreference> =
        combine(wifi, mobile) { w, m ->
            when {
                w && m -> NetworkPreference.BOTH_CONNECTED
                w -> NetworkPreference.WIFI_ONLY
                m -> NetworkPreference.MOBILE_DATA_ONLY
                else -> NetworkPreference.NONE
            }
        }

    override fun isVpnActive(): Boolean = vpn.value
}
