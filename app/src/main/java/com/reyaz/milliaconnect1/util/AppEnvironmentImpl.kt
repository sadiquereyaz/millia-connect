package com.reyaz.milliaconnect1.util

import com.reyaz.core.common.env.AppEnvironment
import com.reyaz.milliaconnect1.BuildConfig

class AppEnvironmentImpl : AppEnvironment {
    override val isDebug: Boolean = BuildConfig.DEBUG
}
