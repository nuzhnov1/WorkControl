package com.nuzhnov.workcontrol.shared.visitservice.domen.repository

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import kotlinx.coroutines.flow.StateFlow

internal interface ControlServiceRepository {
    val state: StateFlow<ControlServiceState>
    val name: StateFlow<String?>

    fun updateState(state: ControlServiceState)
    fun updateName(name: String?)
    suspend fun startControl()
}
