package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.repository.StudentServiceRepository
import javax.inject.Inject
import android.net.nsd.NsdServiceInfo

internal class AddNsdDiscoveredServiceUseCase @Inject constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke(nsdService: NsdServiceInfo) {
        repository.addNsdDiscoveredService(nsdService)
    }
}
