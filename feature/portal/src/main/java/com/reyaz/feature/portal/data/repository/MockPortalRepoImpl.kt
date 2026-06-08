package com.reyaz.feature.portal.data.repository

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.net.toUri
import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.core.common.utils.Resource
import com.reyaz.core.notification.manager.AppNotificationManager
import com.reyaz.core.notification.model.NotificationData
import com.reyaz.core.notification.utils.NotificationConstant
import com.reyaz.feature.portal.data.local.PortalDataStore
import com.reyaz.feature.portal.data.worker.AutoLoginWorker
import com.reyaz.feature.portal.domain.model.ConnectRequest
import com.reyaz.feature.portal.domain.model.JmiWifiState
import com.reyaz.feature.portal.domain.repository.PortalRepository
import constants.NavigationRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class MockPortalRepoImpl(
    private val dataStore: PortalDataStore,
    private val context: Context,
    private val notificationManager: AppNotificationManager,
) : PortalRepository {

    override suspend fun saveCredential(request: ConnectRequest): Result<Unit> {
        return try {
            val result =
                dataStore.saveCredentials(
                    username = request.username,
                    password = request.password,
                    autoConnect = request.autoConnect
                )
            if (result.isSuccess) {
                Result.success(Unit)
            } else {
                // return Result.failure(result.exceptionOrNull() ?: Exception("Unknown Error"))
                throw Exception(result.exceptionOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun connect(shouldNotify: Boolean): Flow<Resource<String>> = flow {
        val username = dataStore.username.first()
        val password = dataStore.password.first()
        if (username != null && password != null) {
            emit(Resource.Loading("Loading!!"))
            kotlinx.coroutines.delay(1500)
            val it: Resource<String> = Resource.Success("Successfully wifi session restored!")
            emit(it)
            when (it) {
                is Resource.Error -> {
                    dataStore.setLoggedIn(false)
                    Timber.tag(TAG).d("Error while login: ${it.message}")
                    if (shouldNotify)
                        showPortalNotification(
                            title = "Failed to restore session",
                            message = it.message ?: "You were not connected to JMI-WiFi [;_;]"
                        )
                }

                is Resource.Success -> {
                    dataStore.setLoggedIn(true)
                    Timber.tag(TAG).d("Successfully logged in")
                    if (shouldNotify) {
                        showPortalNotification(
                            title = "Wifi session restored",
                            message = it.data ?: "Successfully wifi session restored!"
                        )
                    }
                    if (dataStore.autoConnect.first())
                        AutoLoginWorker.scheduleOneTime(context)
                }

                is Resource.Loading -> {}
            }
        }
    }.flowOn(Dispatchers.IO)

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showPortalNotification(title: String, message: String) {
        notificationManager.showNotification(
            NotificationData(
                id = NotificationConstant.PORTAL_CHANNEL.channelId.hashCode(),
                title = title,
                message = message,
                channelId = NotificationConstant.PORTAL_CHANNEL.channelId,
                channelName = NotificationConstant.PORTAL_CHANNEL.channelName,
                importance = NotificationConstant.PORTAL_CHANNEL.importance,
                destinationUri = NavigationRoute.Portal.getDeepLink().toUri(),
                isSilent = true,
            )
        )
    }

    override suspend fun disconnect(): Result<String> {
        val result: Result<String> = Result.success("Successfully Logged out!")
        dataStore.setLoggedIn(false)
        AutoLoginWorker.cancelOneTime(context)
        return result
    }

    override suspend fun checkJmiWifiConnectionState(): JmiWifiState {
        val isJmiWifi = true
        val isWifiHasInternet = true
        Timber.tag(TAG).d("wifi State: HasInternet: $isWifiHasInternet, IsJmiWifi: $isJmiWifi")
        return if (isJmiWifi) {
            if (isWifiHasInternet) {
                dataStore.setLoggedIn(true)
                JmiWifiState.LOGGED_IN
            } else {
                dataStore.setLoggedIn(false)
                JmiWifiState.NOT_LOGGED_IN
            }
        } else {
            JmiWifiState.NOT_CONNECTED
        }
    }
}

private const val TAG = "PORTAL_REPO_IMPL"
