package com.reyaz.feature.portal.presentation

import com.reyaz.feature.portal.domain.model.PromoCard
import com.reyaz.feature.portal.presentation.components.AutomationType


data class PortalUiState(
    val username: String = "99999",
    val password: String = "sssss",

    val isWifiOn: Boolean = true,
    val automationType: AutomationType = AutomationType.WORK_MANAGER,
    val isWifiPrimary: Boolean = false,  // this property is necessary for mobile internet off warning

    val loadingMessage: String? = null,

    val supportingText: String? = null,
//    val supportingText: String? = "You're not connected to Jamia Wifi.\nPlease connect and try again.",
    val isError: Boolean = false,
//    val isError: Boolean = true,

    val promoCard: List<PromoCard> = emptyList(),
    val isConnected: Boolean = false
) {
    val loginBtnEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
    val isLoading: Boolean = !loadingMessage.isNullOrBlank()
}

// not connected to preferred wifi
// connected but not logged in
// logged in
// loading