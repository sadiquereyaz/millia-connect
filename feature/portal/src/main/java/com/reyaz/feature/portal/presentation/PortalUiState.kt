package com.reyaz.feature.portal.presentation

import com.reyaz.feature.portal.domain.model.PromoCard
import com.reyaz.feature.portal.domain.model.defaultPromoCard


data class PortalUiState(
    val username: String = "",
    val password: String = "",

    val isWifiOn: Boolean = true,
    val autoConnect: Boolean = true,
    val isWifiPrimary: Boolean = false,  // this property is necessary for mobile internet off warning

    val loadingMessage: String? = "Loading...",

    val supportingText: String? = null,
//    val supportingText: String? = "You're not connected to Jamia Wifi.\nPlease connect and try again.",
    val isError: Boolean = false,
//    val isError: Boolean = true,

    val promoCard: List<PromoCard> = emptyList()
) {
    val loginBtnEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
    val isLoading: Boolean = !loadingMessage.isNullOrBlank()
}

// not connected to preferred wifi
// connected but not logged in
// logged in
// loading