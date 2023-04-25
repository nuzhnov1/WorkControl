package com.nuzhnov.workcontrol.shared.studentservice.data.repository

import com.nuzhnov.workcontrol.shared.studentservice.data.api.VisitorApi
import com.nuzhnov.workcontrol.shared.studentservice.data.datasource.StudentServiceLocalDataSource
import com.nuzhnov.workcontrol.shared.studentservice.data.datasource.NsdDiscoveredServicesLocalDataSource
import com.nuzhnov.workcontrol.shared.studentservice.data.mapper.toServiceState
import com.nuzhnov.workcontrol.shared.studentservice.data.mapper.toDiscoveredServicesSet
import com.nuzhnov.workcontrol.shared.studentservice.domen.repository.StudentServiceRepository
import com.nuzhnov.workcontrol.shared.studentservice.domen.model.DiscoveredService
import com.nuzhnov.workcontrol.shared.studentservice.domen.model.StudentServiceState
import com.nuzhnov.workcontrol.shared.util.di.annotation.ApplicationCoroutineScope
import com.nuzhnov.workcontrol.shared.util.di.annotation.IODispatcher
import java.net.InetAddress
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import android.net.nsd.NsdServiceInfo

internal class StudentServiceRepositoryImpl @Inject constructor(
    private val api: VisitorApi,
    private val studentServiceLocalDataSource: StudentServiceLocalDataSource,
    private val nsdDiscoveredServicesLocalDataSource: NsdDiscoveredServicesLocalDataSource,
    @ApplicationCoroutineScope applicationCoroutineScope: CoroutineScope,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : StudentServiceRepository {

    override val discoveredServicesFlow = nsdDiscoveredServicesLocalDataSource
        .nsdServicesFlow.map { nsdServicesMap -> nsdServicesMap.toDiscoveredServicesSet() }

    override val serviceState = studentServiceLocalDataSource.serviceState


    init {
        api.visitorState
            .onEach { visitorState ->
                updateServiceState(state = visitorState.toServiceState() ?: return@onEach)
            }
            .launchIn(scope = applicationCoroutineScope)
    }


    override fun updateServiceState(state: StudentServiceState) {
        studentServiceLocalDataSource.updateServiceState(state)
    }

    override fun updateStudentID(id: Long?) {
        studentServiceLocalDataSource.updateStudentID(id)
    }

    override fun getDiscoveredServices(): Set<DiscoveredService> =
        nsdDiscoveredServicesLocalDataSource.getNsdServices().toDiscoveredServicesSet()

    override fun getNsdDiscoveredServicesMap(): Map<String, NsdServiceInfo> =
        nsdDiscoveredServicesLocalDataSource.getNsdServices()

    override fun addNsdDiscoveredService(nsdService: NsdServiceInfo) {
        nsdDiscoveredServicesLocalDataSource.addService(service = nsdService)
    }

    override fun removeNsdDiscoveredService(nsdService: NsdServiceInfo) {
        nsdDiscoveredServicesLocalDataSource.removeDiscoveredService(service = nsdService)
    }

    override fun clearNsdDiscoveredServices() {
        nsdDiscoveredServicesLocalDataSource.clearDiscoveredServices()
    }

    override suspend fun startVisit(serverAddress: InetAddress, serverPort: Int) =
        withContext(context = coroutineDispatcher) {
            api.startVisit(
                serverAddress = serverAddress,
                serverPort = serverPort,
                visitorID = studentServiceLocalDataSource.studentID.value ?: return@withContext
            )
        }
}
