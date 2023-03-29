package com.nuzhnov.workcontrol.shared.visitservice.domen.repository

import com.nuzhnov.workcontrol.core.visitcontrol.model.Visitor
import com.nuzhnov.workcontrol.shared.visitservice.domen.model.VisitControlServiceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

internal interface VisitControlServiceRepository {
    val serviceState: StateFlow<VisitControlServiceState>
    val visitors: Flow<Set<Visitor>>

    fun updateServiceState(serviceState: VisitControlServiceState)
    fun startVisitControlServer(address: InetAddress, port: Int)
    fun clearVisitors()
}
