package com.reyaz.feature.portal.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.core.common.utils.Resource
import com.reyaz.feature.portal.data.local.PortalDataStore
import com.reyaz.feature.portal.data.repository.JmiWifiState
import com.reyaz.feature.portal.domain.repository.PortalRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "PORTAL_VM"
private const val LOGGING = true

class PortalViewModel(
    private val repository: PortalRepository,
    private val networkObserver: NetworkManager,
    private val userPreferences: PortalDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(PortalUiState())
    val uiState: StateFlow<PortalUiState> = _uiState.asStateFlow()

    private var mobileDataJob: Job? = null

    init {
        observeNetworkAndInitialize()
    }

    private fun log(message: String) {
        if (LOGGING) Log.d(TAG, message)
    }

    private fun observeNetworkAndInitialize() {
        try {
            /*viewModelScope.launch {
                userPreferences.isLoggedIn.collect {isLoggedIn->
//                    if (!it)
                        _uiState.update {
                            it.copy(
                                isLoggedIn = isLoggedIn,
                                loadingMessage = null,
                                errorMsg = null
                            )
                        }
                }
            }*/
            viewModelScope.launch {
                Log.d(TAG, "observeNetworkAndInitialize: $uiState")
                networkObserver.observeWifiConnectivity().collect { isWifiConnected ->
                    _uiState.update { it.copy(loadingMessage = "Loading...") }
                    fetchStoredCredentials()
                    if (isWifiConnected) {
                        log("wifi connected")
                        checkConnectionAndLogin()

                        if (!uiState.value.isWifiPrimary) {
                            log("Observing mobile data since Wifi not primary")
                            // Cancel any previous observer to avoid duplicates
                            mobileDataJob?.cancel()

                            mobileDataJob = launch {
                                networkObserver.observeMobileDataConnectivity()
                                    .collect { isMobileData ->
                                        if (!isMobileData) {
                                            log("mobile data off, stopping observation and handling login")
                                            updateState(
                                                loadingMessage = null,
                                                isWifiPrimary = true,
                                                errorMsg = null
                                            )
                                            delay(3000)
                                            performLogin()
                                            // Stop observing since mobile data is off
                                            this.cancel()
                                            log("Mobile observation stopped")
                                        } else {
                                            log("mobile on")
                                        }
                                    }
                            }
                        }
                    } else {
                        log("Wifi not connected")
                        updateState(
                            isJamiaWifi = false,
                            isLoggedIn = false,
                            loadingMessage = null
                        )
                        // Cancel mobile data observation if wifi is off
                        mobileDataJob?.cancel()
                        mobileDataJob = null
                        log("Mobile observation stopped: end")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in observeNetworkAndInitialize", e)
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

    private suspend fun performLogin() {

        repository.connect(shouldNotify = false).collect { result ->
            Log.d(TAG, "performLogin Result: $result")
            when (result) {
                is Resource.Loading -> updateState(
                    errorMsg = null,
                    loadingMessage = "Loading..."
                )

                is Resource.Success -> {
                    updateState(
                        isLoggedIn = true,
                        loadingMessage = null,
                        errorMsg = result.message,
                        isWifiPrimary = result.message.isNullOrBlank()
                    )
//                    saveCredentials()
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            loadingMessage = null,
                            errorMsg = result.message
                        )
                    }
                    Log.e(TAG, "Error in performLogin(): ${result.message}")
//                    saveCredentials()
                }
            }
        }
//        }
    }

    fun logout() {
        viewModelScope.launch {
            updateState(loadingMessage = "Logging Out...")
            repository.disconnect()
                .onSuccess {
                    log("Logout successful")
                    updateState(isLoggedIn = false, loadingMessage = null, errorMsg = it)
//                    saveCredentials()
                }
                .onFailure {
                    Log.e(TAG, "Logout failed", it)
                    handleError(it)
                }
        }
    }

    fun retry() {
        viewModelScope.launch {
            updateState(loadingMessage = "Retrying...", errorMsg = null)
            checkConnectionAndLogin()
        }
    }

    private suspend fun checkConnectionAndLogin() {
        if (!uiState.value.loginBtnEnabled) {
            _uiState.update { it.copy(isJamiaWifi = true, isLoggedIn = false, loadingMessage = null, errorMsg = "One time credential needed to login automatically.") }
        } else {
            log("checking connection state")
            when (repository.checkConnectionState()) {
                JmiWifiState.NOT_CONNECTED -> {
                    log("Not connected")
                    updateState(
                        isJamiaWifi = false,
                        isLoggedIn = false,
                        loadingMessage = null,
                        errorMsg = null
                    )
                }

                JmiWifiState.NOT_LOGGED_IN -> {
                    log("Not logged in")
                    updateState(
                        isJamiaWifi = true,
                        isLoggedIn = false,
                        loadingMessage = null,
                        errorMsg = null
                    )
                   performLogin()
                }

                JmiWifiState.LOGGED_IN -> {
                    log("Already Logged in")
                    updateState(
                        isJamiaWifi = true,
                        isLoggedIn = true,
                        loadingMessage = null,
                        errorMsg = null
                    )
                }
            }
        }
//        }
    }

    private fun handleError(exception: Throwable) {
        val message = when {
            exception.message?.contains("10.2.0.10:8090") == true ->
                "You're not connected to Jamia Wifi.\nPlease connect and try again."

            exception.message?.contains("Wrong Username or Password") == true ->
                "Wrong Username or Password"

            else -> exception.message ?: "Oops! An error occurred."
        }

        updateState(loadingMessage = null, errorMsg = message)
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
        saveCredentials()
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
        saveCredentials()
    }

    fun updateAutoConnect(autoConnect: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(autoConnect = autoConnect) }
            userPreferences.setAutoConnect(autoConnect)
            saveCredentials()
        }
    }

    private fun saveCredentials() {
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
