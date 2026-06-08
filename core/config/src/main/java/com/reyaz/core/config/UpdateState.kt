package com.reyaz.core.config

import android.content.Context

sealed interface UpdateState {
    object None : UpdateState
    data class Optional(val message: String) : UpdateState
    data class Force(val message: String) : UpdateState
}