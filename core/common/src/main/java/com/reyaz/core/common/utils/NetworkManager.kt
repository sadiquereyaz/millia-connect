package com.reyaz.core.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.CaptivePortal
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

private const val TAG = "NETWORK_MANAGER"

@SuppressLint("MissingPermission")
class NetworkManager(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var captivePortal: CaptivePortal? = null
    private var wifiForceCallback: ConnectivityManager.NetworkCallback? = null

    // todo: integrate launch with captive portal intent
    fun setCaptivePortal(intent: Intent?) {
        captivePortal = intent?.getParcelableExtra(ConnectivityManager.EXTRA_CAPTIVE_PORTAL)
        Timber.tag(TAG).d("CaptivePortal object received: ${captivePortal != null}")
    }

    fun reportCaptivePortalDismissed() {
        try{
            captivePortal?.let {
                Timber.tag(TAG).d( "Reporting captive portal dismissed")
                it.reportCaptivePortalDismissed()
                captivePortal = null
            }
        } catch (e: Exception){
            Timber.tag(TAG).d( "Error while dismissing Captive Portal: ${e.message}")
        }
    }

    /**
     * Binds the current process to a Wi-Fi network.
     *
     * This function requests a Wi-Fi network with internet capability.
     * When a suitable Wi-Fi network becomes available, the process is bound to it.
     * If the Wi-Fi network is lost, the process binding is reset.
     *
     * It also ensures that any previously registered network callback for forcing Wi-Fi
     * is unregistered before registering a new one.
     */
    fun bindToWifiNetwork() {
        Timber.tag(TAG).d( "Binding to Wi-Fi network...")

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectivityManager.bindProcessToNetwork(network)
                Timber.tag(TAG).d( "Wi-Fi network bound")
            }

            override fun onLost(network: Network) {
                connectivityManager.bindProcessToNetwork(null)
                Timber.tag(TAG).d( "Wi-Fi network lost, reset binding")
            }
        }

        // Cleanup previous callback if any
        wifiForceCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }

        wifiForceCallback = callback
        connectivityManager.requestNetwork(request, callback)
    }

    /**
     * Resets the network binding to the default (null).
     * This function unbinds the process from any specific network and
     * unregisters the `wifiForceCallback` if it was previously set.
     */
    fun resetNetworkBinding() {
        Timber.tag(TAG).d( "Resetting network binding")
        connectivityManager.bindProcessToNetwork(null)
        wifiForceCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
            wifiForceCallback = null
        }
    }

    fun observeWifiConnectivity(): Flow<Boolean> =
        observeConnectivity(NetworkCapabilities.TRANSPORT_WIFI)

     fun observeMobileDataConnectivity(): Flow<Boolean> =
        observeConnectivity(NetworkCapabilities.TRANSPORT_CELLULAR)

    fun observeCaptivePortalConnectivity(): Flow<Boolean> = callbackFlow {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
                val isCaptive = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)
                trySend(isCaptive)
                Timber.tag(TAG).d("Captive portal changed: $isCaptive")
            }

            override fun onLost(network: Network) {
                trySend(false)
                Timber.tag(TAG).d("Captive portal lost")
            }
        }

        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
            Timber.tag(TAG).d("Captive portal callback unregistered")
        }
    }.distinctUntilChanged()

    /**
     * Observes the connectivity status for a specific network transport type.
     *
     * This function uses a [callbackFlow] to emit `true` when the specified transport type is available
     * and `false` when it is lost or unavailable. It registers a [ConnectivityManager.NetworkCallback]
     * to listen for network changes and updates the flow accordingly.
     *
     * The flow also emits the initial connectivity state upon subscription.
     *
     * The [distinctUntilChanged] operator ensures that only distinct connectivity states are emitted.
     *
     * @param transportType The network transport type to observe (e.g., [NetworkCapabilities.TRANSPORT_WIFI], [NetworkCapabilities.TRANSPORT_CELLULAR]).
     * @return A [Flow] of [Boolean] indicating the connectivity status (true if connected, false otherwise).
     */
    private fun observeConnectivity(transportType: Int): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                val hasTransport = capabilities?.hasTransport(transportType) == true
//                trySend(hasTransport)
                trySend(true)
                Timber.tag(TAG).d( "onAvailable($transportType)")
            }

            override fun onLost(network: Network) {
                // Only emit false if this was the last network of this type
                val stillConnected = isAnyNetworkOfTypeAvailable(transportType)
//                trySend(stillConnected)
                trySend(false)
                Timber.tag(TAG).d( "onLost: $transportType, still connected: $stillConnected")
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
//                val hasTransport = capabilities.hasTransport(transportType)
//                trySend(hasTransport)
//                Timber.tag(TAG).d( "onCapabilitiesChanged: $transportType = $hasTransport")
            }
        }

        val request = NetworkRequest.Builder()
            .addTransportType(transportType)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Get initial state more comprehensively
        val connected = isAnyNetworkOfTypeAvailable(transportType)
        trySend(connected)
        Timber.tag(TAG).d( "Initial state: $transportType = $connected")

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
            Timber.tag(TAG).d( "NetworkCallback unregistered: $transportType")
        }
    }.distinctUntilChanged()

    fun observeAllNetworkType(): Flow<NetworkPreference> = combine(
        observeWifiConnectivity(),
        observeMobileDataConnectivity()
    ) { isWifiConnected, isMobileDataConnected ->
        when {
            isWifiConnected && isMobileDataConnected -> {
                Timber.tag(TAG).d( "Both wifi and mobile data connected")
                NetworkPreference.BOTH_CONNECTED
            }
            isWifiConnected -> {
                Timber.tag(TAG).d( "Only wifi connected")
                NetworkPreference.WIFI_ONLY
            }
            isMobileDataConnected -> {
                Timber.tag(TAG).d( "Only mobile data connected")
                NetworkPreference.MOBILE_DATA_ONLY
            }
            else -> {
                Timber.tag(TAG).d( "No connection")
                NetworkPreference.NONE
            }
        }
    }

    fun observeInternetConnectivity(): Flow<Boolean> = callbackFlow {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
                Timber.tag(TAG).d( "Internet connectivity available")
            }

            override fun onLost(network: Network) {
                trySend(false)
                Timber.tag(TAG).d( "Internet connectivity lost")
            }
        }

        connectivityManager.registerNetworkCallback(request, callback)

        // Emit initial connectivity state
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        val hasInternet = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        trySend(hasInternet)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
            Timber.tag(TAG).d( "Internet NetworkCallback unregistered")
        }
    }.distinctUntilChanged()




    /**
     * Checks if any network of the specified transport type is available,
     * not just the active network.
     */
    private fun isAnyNetworkOfTypeAvailable(transportType: Int): Boolean {
        val networks = connectivityManager.boundNetworkForProcess?.let { listOf(it) }
            ?: listOfNotNull(connectivityManager.activeNetwork)

        return networks.any { network ->
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasTransport(transportType) == true
        }
    }

}

enum class NetworkPreference {
    WIFI_ONLY,
    MOBILE_DATA_ONLY,
    BOTH_CONNECTED,
    NONE
}
