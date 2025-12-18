package com.reyaz.core.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class AppViewModel(
    private val updateChecker: AppUpdateChecker
) : ViewModel() {

    /**
     * A [StateFlow] that emits the current app update state.
     *
     * This flow is initialized eagerly when the ViewModel is created. It performs a one-time check
     * for available app updates using [AppUpdateChecker] and emits the resulting [UpdateState].
     * The initial value is [UpdateState.None] until the check is complete.
     */
    val updateState = flow {
        emit(updateChecker.check())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = UpdateState.None
    )
}
