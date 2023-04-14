package com.nuzhnov.workcontrol.shared.visitservice.domen.repository

import com.nuzhnov.workcontrol.shared.visitservice.domen.model.Visitor
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.ControlServiceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface ControlServiceRepository {
    val visitors: Flow<Set<Visitor>>
    val serviceState: StateFlow<ControlServiceState>
    val serviceName: StateFlow<String?>


    suspend fun getVisitors(): Set<Visitor>

    fun updateServiceState(state: ControlServiceState)

    fun updateServiceName(name: String?)

    suspend fun startControl()

    suspend fun clearPersistVisitors()
}
