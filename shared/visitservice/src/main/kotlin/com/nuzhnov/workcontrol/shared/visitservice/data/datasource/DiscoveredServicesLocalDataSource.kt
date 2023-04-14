package com.nuzhnov.workcontrol.shared.visitservice.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

internal class DiscoveredServicesLocalDataSource @Inject constructor() {
    private val _discoveredServices = MutableStateFlow<Set<String>>(value = setOf())
    val discoveredServices: Flow<Set<String>> = _discoveredServices


    fun getDiscoveredServices() = _discoveredServices.value

    fun addDiscoveredService(discoveredService: String) {
        _discoveredServices.applyUpdate { add(discoveredService) }
    }

    fun removeDiscoveredService(discoveredService: String) {
        _discoveredServices.applyUpdate { remove(discoveredService) }
    }

    fun clearDiscoveredServices() {
        _discoveredServices.applyUpdate { clear() }
    }

    private fun MutableStateFlow<Set<String>>.applyUpdate(
        block: MutableSet<String>.() -> Unit
    ) {
        value = value.toMutableSet().apply(block)
    }
}
