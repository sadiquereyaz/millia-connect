package com.reyaz.core.config

import android.content.Context
import android.os.Build

interface AppVersionProvider {
    fun versionCode(): Int
}

class DefaultAppVersionProvider(
    private val context: Context
) : AppVersionProvider {

    override fun versionCode(): Int {
        val packageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toInt()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode
        }
    }
}

