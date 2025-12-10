package com.reyaz.feature.portal.presentation


data class PortalUiState(
    val username: String = "",
    val password: String = "",

    val isLoggedIn: Boolean = false,
    val isJamiaWifi: Boolean = true,
    val autoConnect: Boolean = true,
    val isWifiPrimary: Boolean = true,

    val loadingMessage: String? = "Loading...",
    val errorMsg: String? = null,

    val supportingText: String? = null,
) {
    val loginBtnEnabled: Boolean = username.isNotEmpty() && password.isNotEmpty()
    val isLoading: Boolean = !loadingMessage.isNullOrBlank()
    val conditionalErrorMsg: String? = if (!isJamiaWifi) {
        "You're not connected to Jamia Wifi.\nPlease connect and try again."
    } else if (!isWifiPrimary) {
        "Please turn you're internet off!"
    } else if (!errorMsg.isNullOrBlank()) {
        errorMsg
    } else null
}

// not connected to preferred wifi
// connected but not logged in
// logged in
// loading