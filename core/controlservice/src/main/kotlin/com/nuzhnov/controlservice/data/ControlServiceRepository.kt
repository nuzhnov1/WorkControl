package com.nuzhnov.controlservice.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class ControlServiceRepository {

    private val _serviceState = MutableStateFlow<ControlServiceState?>(value = null)
    val serviceState = _serviceState.asStateFlow()


    internal fun setServiceState(serviceState: ControlServiceState) {
        _serviceState.value = serviceState
    }
}
