package com.reyaz.core.analytics

interface AnalyticsTracker {
    fun logEvent(eventName: String, params: Map<String, Any>? = null)
    fun setUserProperty(name: String, value: String)
}