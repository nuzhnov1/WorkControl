package com.nuzhnov.controlservice.data.repository

import com.nuzhnov.controlservice.data.model.Client
import com.nuzhnov.controlservice.data.model.Role
import com.nuzhnov.controlservice.data.model.ServiceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.time.LocalTime
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class ServiceRepository {

    protected abstract val coroutineScope: CoroutineScope

    private val _role = MutableStateFlow<Role?>(value = null)
    val role = _role.asStateFlow()

    private val _serviceState = MutableStateFlow<ServiceState?>(value = null)
    val serviceState = _serviceState.asStateFlow()

    private val _clients = MutableStateFlow(value = mutableMapOf<Long, Client>())
    val clients: StateFlow<Map<Long, Client>> = _clients


    fun setRole(role: Role) {
        _role.value = role
    }

    fun updateServiceState(serviceState: ServiceState) {
        _serviceState.value = serviceState
    }

    @OverridingMethodsMustInvokeSuper
    open fun updateClient(uniqueID: Long, isActiveNow: Boolean) {
        _clients.update { clients -> clients.apply { updateClient(uniqueID, isActiveNow) } }
    }

    @OverridingMethodsMustInvokeSuper
    open fun removeAllClients() {
        _clients.update { clients -> clients.apply { clear() } }
    }

    // TODO: replace time methods with those that support api below 26
    private fun MutableMap<Long, Client>.updateClient(uniqueID: Long, isActiveNow: Boolean) {
        val client = this[uniqueID]
        val now = LocalTime.now()

        if (client?.lastVisit == null) {
            this[uniqueID] = Client(
                uniqueID = uniqueID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { null },
                totalVisitDuration = Duration.ZERO
            )
        } else {
            val isPreviouslyActive = client.isActive
            val previouslyVisit = client.lastVisit
            val duration = client.totalVisitDuration
            val delta = Duration.between(previouslyVisit, now)

            this[uniqueID] = Client(
                uniqueID = uniqueID,
                isActive = isActiveNow,
                lastVisit = if (isActiveNow) { now } else { previouslyVisit },
                totalVisitDuration = if (isPreviouslyActive) { duration + delta } else { duration }
            )
        }
    }
}
