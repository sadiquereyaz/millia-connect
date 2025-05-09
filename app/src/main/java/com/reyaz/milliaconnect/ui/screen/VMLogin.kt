package com.reyaz.milliaconnect.ui.screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.milliaconnect.data.UserPreferences
import com.reyaz.milliaconnect.data.WebLoginManager
import com.reyaz.milliaconnect.util.NetworkConnectivityObserver
import com.reyaz.milliaconnect.worker.AutoLoginWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VMLogin(
    private val userPreferences: UserPreferences,
    private val webLoginManager: WebLoginManager,
    private val networkObserver: NetworkConnectivityObserver,
    private val appContext: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateLogin())
    val uiState: StateFlow<UiStateLogin> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Observe WiFi connectivity
            networkObserver.observeWifiConnectivity()
                .collect { isWifiConnected ->
                    _uiState.update {
                        it.copy(
                            showNoWifiDialog = !isWifiConnected
                        )
                    }
                    if (isWifiConnected) {
                        _uiState.update {
                            it.copy(
                                username = userPreferences.username.first(),
                                password = userPreferences.password.first(),
                                isLoggedIn = userPreferences.loginStatus.first(),
                                autoConnect = userPreferences.autoConnect.first()
                            )
                        }
                        if (uiState.value.loginEnabled) handleLogin()
                        else _uiState.update { it.copy(message = "One time credential needed to login automatically.") }
                    }
                }
        }
    }

    fun handleLogin() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingMessage = "Logging in...") }
            webLoginManager.performLogin(_uiState.value.username, _uiState.value.password)
                .onSuccess { message ->
//                    Log.d("VMLogin", "Login successful")
                    _uiState.update {
                        it.copy(
                            message = message,
                            isLoggedIn = true,
                            loadingMessage = null
                        )
                    }
                    saveCredentials(true)
                    if (_uiState.value.autoConnect)
                        AutoLoginWorker.schedule(context = appContext)
                }
                .onFailure { exception ->
                    AutoLoginWorker.cancel(appContext)
                    onError(exception)
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingMessage = "Logging Out...") }
            webLoginManager.performLogout()
                .onSuccess { message ->
                    Log.d("VMLogin", "Logout successful")
                    _uiState.update {
                        it.copy(
                            isLoggedIn = false,
                            loadingMessage = null,
                            message = message
                        )
                    }
                    saveCredentials(false)
                }
                .onFailure { exception ->
                    Log.e("VMLogin", "Logout failed", exception)
                    onError(exception)
                }
            AutoLoginWorker.cancel(appContext)
        }
    }

    private fun onError(exception: Throwable) {
        viewModelScope.launch {
            val error = if (exception.message?.contains("10.2.0.10:8090") == true)
                "You're not connected to Jamia Wifi.\nPlease connect and try again."
            else if (exception.message?.contains("Wrong Username or Password") == true)
                "Wrong Username or Password"
            else /*exception.message ?: */ "Oops! An error occurred."

            _uiState.update {
                it.copy(
                    loadingMessage = null,
                    message = error
                )
            }

        }
    }

    fun dismissNoWifiDialog() {
        _uiState.update { it.copy(showNoWifiDialog = false) }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
        viewModelScope.launch {
            saveCredentials(isLoggedIn = false)
        }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
        viewModelScope.launch {
            saveCredentials(isLoggedIn = false)
        }
    }

    fun updateAutoConnect(autoConnect: Boolean, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(autoConnect = autoConnect) }
            if (!uiState.value.autoConnect)
                AutoLoginWorker.cancel(context = context)
            else if (uiState.value.isLoggedIn && uiState.value.loginEnabled){
                AutoLoginWorker.schedule(context = context)
            }
            userPreferences.setAutoConnect(autoConnect)
        }
        viewModelScope.launch {
            saveCredentials(isLoggedIn = false)
        }
    }

    private fun saveCredentials(isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreferences.saveCredentials(
                _uiState.value.username,
                _uiState.value.password,
                isLoggedIn,
                _uiState.value.autoConnect
            )
        }
    }
}

