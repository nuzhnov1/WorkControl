package com.nuzhnov.workcontrol.shared.studentservice.domen.usecase.internal

import com.nuzhnov.workcontrol.shared.studentservice.domen.repository.StudentServiceRepository
import javax.inject.Inject
import android.net.nsd.NsdServiceInfo

internal class GetNsdDiscoveredServicesMapUseCase @Inject constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke(): Map<String, NsdServiceInfo> = repository.getNsdDiscoveredServicesMap()
}
