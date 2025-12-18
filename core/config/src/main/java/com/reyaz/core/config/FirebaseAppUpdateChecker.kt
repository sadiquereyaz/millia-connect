package com.reyaz.core.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import timber.log.Timber

interface AppUpdateChecker {
    suspend fun check(): UpdateState
}

class FirebaseAppUpdateChecker(
    private val remoteConfig: FirebaseRemoteConfig,
    private val versionProvider: AppVersionProvider
) : AppUpdateChecker {

    override suspend fun check(): UpdateState {
        try {
            remoteConfig.fetchAndActivate().await()

            val minVersion = remoteConfig.getLong("min_app_version").toInt()
            val force = remoteConfig.getBoolean("force_update")
            val message = remoteConfig.getString("update_message")

            val currentVersion = versionProvider.versionCode()

            Timber.tag("AppUpdateChecker")
                .d("remoteMinVersion (minVersion) $minVersion, currentLocalVersion (currentVersion) $currentVersion, force $force, message $message")

            return when {
                currentVersion < minVersion && force -> {
                    Timber.tag("AppUpdateChecker").d("Force update required")
                    UpdateState.Force(message)
                }

                currentVersion < minVersion -> {
                    Timber.tag("AppUpdateChecker").d("Optional update required")
                    UpdateState.Optional(message)
                }

                else -> {
                    Timber.tag("AppUpdateChecker").d("No update required")
                    UpdateState.None
                }
            }
        } catch (e: Exception) {
            Timber.tag("AppUpdateChecker").e("Error checking for updates: ${e.message}")
            println("Hello" + e.message)
            return UpdateState.None

        }
    }
}
