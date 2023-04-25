package com.nuzhnov.workcontrol.core.studentservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.studentservice.domen.repository.StudentServiceRepository
import javax.inject.Inject
import android.net.nsd.NsdServiceInfo

internal class GetNsdDiscoveredServicesMapUseCase @Inject constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke(): Map<String, NsdServiceInfo> = repository.getNsdDiscoveredServicesMap()
}
