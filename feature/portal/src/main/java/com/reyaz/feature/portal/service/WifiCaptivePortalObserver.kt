package com.reyaz.feature.portal.service

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.reyaz.feature.portal.service.PortalLoginForegroundService
import timber.log.Timber

class WifiCaptivePortalObserver(
    private val context: Context
) {

//    private val cm =
//        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun start() {
        Timber.d("start()")
        startForegroundLogin()
        /*val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        cm.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                val caps = cm.getNetworkCapabilities(network) ?: return

                val isValidated =
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

                val isCaptivePortal =
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)

                Timber.d("Is Captive Portal: $isCaptivePortal --- isValidated: $isValidated")

                if (!isValidated) {
                    Timber.d("starting foreground service")
                    startForegroundLogin()
                }
            }
        })*/
    }
    fun stop(){
        // todo:
    }

    private fun startForegroundLogin() {
        val intent = Intent(context, PortalLoginForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Timber.tag("WifiCaptivePortalObserver").d("startForegroundService()")
            context.startForegroundService(intent)
        } else {
            Timber.tag("WifiCaptivePortalObserver").d("startService()")
            context.startService(intent)
        }
    }
}
