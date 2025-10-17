package com.reyaz.core.analytics

import android.annotation.SuppressLint
import com.google.firebase.analytics.FirebaseAnalytics
import android.os.Bundle
import android.app.Application

class FirebaseAnalyticsTracker(application: Application) : AnalyticsTracker {
    @SuppressLint("MissingPermission")
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(application)

    override fun logEvent(eventName: String, params: Map<String, Any>?) {
        val bundle = Bundle()
        params?.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                else -> bundle.putString(key, value.toString())
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }
}