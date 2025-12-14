package com.reyaz.feature.portal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.analytics.AnalyticsTracker
import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.core.common.utils.Resource
import com.reyaz.feature.portal.data.local.PortalDataStore
import com.reyaz.feature.portal.data.repository.JmiWifiState
import com.reyaz.feature.portal.domain.repository.PortalRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TAG = "PORTAL_VM"
private const val LOGGING = true

class PortalViewModel(
    private val repository: PortalRepository,
    private val networkManager: NetworkManager,
    private val userPreferences: PortalDataStore,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(PortalUiState())
    val uiState: StateFlow<PortalUiState> = _uiState.asStateFlow()

    private var mobileDataJob: Job? = null

    init {
        observeNetworkAndInitialize()
    }

    private fun observeNetworkAndInitialize() {
        try {
            // todo: Replace viewModelScope by globalScope
            viewModelScope.launch {
                fetchStoredCredentials()
                Timber.tag(TAG).d("observeNetworkAndInitialize: $uiState")
                networkManager.observeWifiConnectivity().collect { isWifiConnected ->
                    if (isWifiConnected) {
                        Timber.d("wifi connected")
//                        checkJmiWifiConnectionAndLogin()
                        attemptLoginAndCheckJmiWifiConnectionState()
                        if (!uiState.value.isWifiPrimary) {
                            // Wifi is not primary, observe mobile data
                            Timber.d("Observing mobile data since Wifi not primary")
                            // Cancel any previous observer to avoid duplicates
                            mobileDataJob?.cancel()

                            mobileDataJob = launch {
                                networkManager.observeMobileDataConnectivity()
                                    .collect { isMobileData ->
                                        if (!isMobileData) {
                                            Timber.d("mobile data off, stopping observation and handling login")
                                            _uiState.update {
                                                it.copy(
                                                    isWifiPrimary = true,
                                                    supportingText = JmiWifiState.LOGGED_IN.supportingMsg,
                                                    isError = false
                                                )
                                            }
                                            // Stop observing since mobile data is off
                                            this.cancel()
                                            Timber.d("Mobile observation stopped")
                                        } else {
                                            Timber.d("mobile on")
                                        }
                                    }
                            }
                        }
                    } else {
                        Timber.d("Wifi not connected")
                        _uiState.update {
                            it.copy(
                                loadingMessage = null,
                                isError = true,
                                supportingText = "You're not connected to Jamia Wifi.\nPlease connect and try again.",
                            )
                        }
                        updateState(
                            isJamiaWifi = false,
                            isLoggedIn = false,
                            loadingMessage = null,

                            )
                        // Cancel mobile data observation if wifi is off
                        mobileDataJob?.cancel()
                        mobileDataJob = null
                        Timber.d("Mobile observation stopped: end")
                    }
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Error in observeNetworkAndInitialize")
        }
    }

    private suspend fun fetchStoredCredentials() {
        _uiState.update {
            it.copy(
                username = userPreferences.username.first() ?: "",
                password = userPreferences.password.first() ?: "",
                autoConnect = userPreferences.autoConnect.first()
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingMessage = null,
                    isError = false,
                    supportingText = "Successfully Logged Out!"
                )
            }
            repository.disconnect()
                .onSuccess {
                    Timber.d("Logout successful")
                }
                .onFailure {
                    Timber.tag(TAG).e(it, "Logout failed")
                    analyticsTracker.logEvent(
                        "portal_logout_failed",
                        mapOf(
                            "username" to _uiState.value.username,
                            "error_message" to (it.message ?: "unknown")
                        )
                    )
                }
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            analyticsTracker.logEvent(
                "portal_retry",
                mapOf("username" to _uiState.value.username)
            )
            saveLoginStateToLocal()
            attemptLoginAndCheckJmiWifiConnectionState()
        }
    }

    private suspend fun attemptLoginAndCheckJmiWifiConnectionState() {
        Timber.d("attempting login first and then checking connection state")
        if (uiState.value.loginBtnEnabled) {
            // username and password are not empty
            repository.connect(shouldNotify = false).collect { result ->
                Timber.tag(TAG).d("performLogin Result: $result")
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                supportingText = null,
                                loadingMessage = "Loading..."
                            )
                        }
                    }

                    is Resource.Success -> {
                        val isWifiPrimary = result.message.isNullOrBlank()
                        _uiState.update {
                            it.copy(
                                supportingText = if (isWifiPrimary) JmiWifiState.LOGGED_IN.supportingMsg else  "You're Logged In!\nTo enjoy Wifi, please turn you're internet off!",
                                isError = false,
                                loadingMessage = null,
                                isWifiPrimary = isWifiPrimary
                            )
                        }

                        // Analytics event for successful login
                        analyticsTracker.logEvent(
                            eventName = "portal_login_success",
                            mapOf(
                                "username" to _uiState.value.username,
                                "wifi_primary" to _uiState.value.isWifiPrimary.toString(),
                                "auto_connect" to _uiState.value.autoConnect.toString(),
                                "is_jamia_wifi" to _uiState.value.isJamiaWifi.toString(),
                                "is_logged_in" to _uiState.value.isLoggedIn.toString(),
                                "error_msg" to result.message.toString(),
                                "loading_msg" to _uiState.value.loadingMessage.toString()
                            )
                        )
                    }

                    is Resource.Error -> {
                        Timber.tag(TAG).e("Error in performLogin(): ${result.message}")
                        val wifiStateResult = repository.checkJmiWifiConnectionState()
                        val isCaptivePortalPageError = wifiStateResult == JmiWifiState.NOT_LOGGED_IN
                        Timber.d(wifiStateResult.name)
                        _uiState.update {
                            it.copy(
                                supportingText = if (isCaptivePortalPageError) result.message else wifiStateResult.supportingMsg,
                                loadingMessage = null,
                                isError = if (isCaptivePortalPageError) true else wifiStateResult.showAsError
                            )
                        }
                        // Analytics event for failed login
                        analyticsTracker.logEvent(
                            "portal_login_failed",
                            mapOf(
                                "username" to _uiState.value.username,
                                "error_message" to (result.message ?: "unknown")
                            )
                        )
                    }
                }
            }
        } else {
            // username and password are empty
            _uiState.update {
                it.copy(
                    loadingMessage = null,
                    supportingText = "One time credential needed to login automatically.",
                    isError = false
                )
            }
        }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun updateAutoConnect(autoConnect: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(autoConnect = autoConnect) }
            userPreferences.setAutoConnect(autoConnect)
        }
    }

    private fun saveLoginStateToLocal() {
        viewModelScope.launch {
            userPreferences.saveCredentials(
                username = _uiState.value.username,
                password = _uiState.value.password,
                autoConnect = _uiState.value.autoConnect
            )
        }
    }

    private fun updateState(
        username: String? = null,
        password: String? = null,
        isLoggedIn: Boolean? = null,
        isJamiaWifi: Boolean? = null,
        autoConnect: Boolean? = null,
        loadingMessage: String? = _uiState.value.loadingMessage,
        errorMsg: String? = _uiState.value.errorMsg,
        isWifiPrimary: Boolean = _uiState.value.isWifiPrimary
    ) {
        _uiState.update {
            it.copy(
                username = username ?: it.username,
                password = password ?: it.password,
                isLoggedIn = isLoggedIn ?: it.isLoggedIn,
                isJamiaWifi = isJamiaWifi ?: it.isJamiaWifi,
                autoConnect = autoConnect ?: it.autoConnect,
                loadingMessage = loadingMessage,
                errorMsg = errorMsg,
                isWifiPrimary = isWifiPrimary
            )
        }
    }

}
