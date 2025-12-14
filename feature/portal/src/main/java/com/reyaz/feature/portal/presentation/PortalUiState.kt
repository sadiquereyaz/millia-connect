package com.reyaz.feature.portal.presentation


data class PortalUiState(
    val username: String = "",
    val password: String = "",

    val isLoggedIn: Boolean = false, // todo: remove
    val isJamiaWifi: Boolean = true, // todo: remove
    val autoConnect: Boolean = true,
    val isWifiPrimary: Boolean = false,  // this property is necessary for mobile internet off warning

    val loadingMessage: String? = "Loading...",
    val errorMsg: String? = null, // todo: remove

    val supportingText: String? = null,
//    val supportingText: String? = "You're not connected to Jamia Wifi.\nPlease connect and try again.",
    val isError: Boolean = false,
//    val isError: Boolean = true,
) {
    val loginBtnEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
    val isLoading: Boolean = !loadingMessage.isNullOrBlank()
}

// not connected to preferred wifi
// connected but not logged in
// logged in
// loading