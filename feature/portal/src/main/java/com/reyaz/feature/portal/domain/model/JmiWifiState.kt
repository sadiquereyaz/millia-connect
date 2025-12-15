package com.reyaz.feature.portal.domain.model

enum class JmiWifiState(val supportingMsg: String? = null, val showAsError: Boolean = false) {
    NOT_CONNECTED(
        "You're not connected to Jamia Wifi.\nPlease connect and try again.",
        showAsError = true
    ),
    NOT_LOGGED_IN(supportingMsg = "You're not logged in."),
    LOGGED_IN(supportingMsg = "You're logged in."),
}