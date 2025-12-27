package com.reyaz.feature.portal.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.reyaz.feature.portal.service.PortalLoginForegroundService

fun createPortalNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            PortalLoginForegroundService.NOTIFICATION_CHANNEL_ID,
            "Wi-Fi Login",
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = "Captive portal login"

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
