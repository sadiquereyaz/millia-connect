package com.reyaz.milliaconnect1

import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.reyaz.core.ui.theme.MilliaConnectTheme
import com.reyaz.milliaconnect1.util.NetworkConnectivityObserver
import timber.log.Timber

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen  = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{false}

        // Check if this activity was launched from a captive portal notification
        if (intent?.action == ConnectivityManager.ACTION_CAPTIVE_PORTAL_SIGN_IN) {
            Timber.d("Captive portal intent received")
            val networkConnectivityObserver = NetworkConnectivityObserver(this)
            networkConnectivityObserver.setCaptivePortal(intent)

            // You might want to automatically trigger login here or wait for user input
        } else
            Timber.d("No Captive Portal Intent Received!")

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            MilliaConnectTheme {
                Surface {
                    MilliaConnectApp()
                }
            }
        }
    }
}