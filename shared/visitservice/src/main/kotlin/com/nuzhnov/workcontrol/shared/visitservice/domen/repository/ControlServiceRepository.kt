package com.nuzhnov.workcontrol.shared.visitservice.domen.repository

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import kotlinx.coroutines.flow.StateFlow

internal interface ControlServiceRepository {
    val serviceState: StateFlow<ControlServiceState>
    val serviceName: StateFlow<String?>


    fun updateServiceState(state: ControlServiceState)

    fun updateServiceName(name: String?)

    suspend fun startControl()
}
