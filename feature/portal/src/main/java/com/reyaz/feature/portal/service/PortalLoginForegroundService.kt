package com.reyaz.feature.portal.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.reyaz.feature.portal.domain.repository.PortalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class PortalLoginForegroundService : Service() {

    private val portalRepository: PortalRepository by inject()

    private lateinit var connectivityManager: ConnectivityManager

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val loginInProgress = AtomicBoolean(false)

    override fun onCreate() {
        super.onCreate()
        Timber.d("PortalLoginForegroundService onCreate")

        connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        startForeground(NOTIFICATION_ID, createNotification())
        registerWifiCallback()
        startPeriodicRefresh()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        Timber.d("PortalLoginForegroundService onStartCommand")
        return START_STICKY
    }

    override fun onDestroy() {
        Timber.d("PortalLoginForegroundService onDestroy")
        connectivityManager.unregisterNetworkCallback(networkCallback)
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ------------------------------------------------------------------------
    // Network callback
    // ------------------------------------------------------------------------

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onCapabilitiesChanged(
            network: Network,
            capabilities: NetworkCapabilities
        ) {
            val isWifi =
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

            val hasInternet =
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            Timber.d("Network changed → wifi=$isWifi internet=$hasInternet")

            if (isWifi && !hasInternet) {
                triggerLogin(network)
            }
        }
    }

    private fun registerWifiCallback() {
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.registerNetworkCallback(
            request,
            networkCallback
        )
    }

    // ------------------------------------------------------------------------
    // Login logic
    // ------------------------------------------------------------------------

    private fun triggerLogin(network: Network) {
        if (!loginInProgress.compareAndSet(false, true)) {
            Timber.d("Login already in progress, skipping")
            return
        }

        serviceScope.launch {
            try {
                Timber.d("Binding process to Wi-Fi network")
                connectivityManager.bindProcessToNetwork(network)

                portalRepository
                    .connect(shouldNotify = true, shouldStartService = false).collect{ result ->
                        Timber.d("Portal login result: $result")
                        /*if(result is Resource.Success){
                            Timber.d("Stopping service using stopSelf()")
                            stopSelf()
                        }*/
                    }

            } catch (e: Exception) {
                Timber.e(e, "Portal login failed")
            } finally {
                connectivityManager.bindProcessToNetwork(null)
                loginInProgress.set(false)
                Timber.d("Login finished, network unbound")
            }
        }
    }

    // ------------------------------------------------------------------------
    // Periodic refresh
    // ------------------------------------------------------------------------

    private fun startPeriodicRefresh() {
        serviceScope.launch {
            while (isActive) {
                delay(55 * 60 * 1000L) // 55 minutes
                restoreSession()
            }
        }
    }

    private fun restoreSession() {
        val network = connectivityManager.activeNetwork ?: return
        val caps =
            connectivityManager.getNetworkCapabilities(network) ?: return

        val isWifi =
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

        Timber.d("Periodic check → wifi=$isWifi")

        if (isWifi) {
            triggerLogin(network)
        }
    }

    // ------------------------------------------------------------------------
    // Notification
    // ------------------------------------------------------------------------

    private fun createNotification(): Notification {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Wi-Fi Auto Login",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager =
                getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Wi-Fi Login Active")
            .setContentText("Maintaining campus Wi-Fi session")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setOngoing(true)
            .build()
    }

    fun stopService(){
        stopSelf()
    }

    companion object {
        const val NOTIFICATION_ID = 101
        const val NOTIFICATION_CHANNEL_ID = "portal_login_channel"
    }
}
