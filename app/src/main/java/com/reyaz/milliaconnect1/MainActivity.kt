package com.reyaz.milliaconnect1

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.work.WorkManager
import com.reyaz.core.common.utils.openUrl
import com.reyaz.core.config.AppViewModel
import com.reyaz.core.config.ForceUpdateDialog
import com.reyaz.core.config.UpdateState
import com.reyaz.core.ui.theme.MilliaConnectTheme
import com.reyaz.milliaconnect1.util.NetworkConnectivityObserver
import org.koin.androidx.compose.koinViewModel
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