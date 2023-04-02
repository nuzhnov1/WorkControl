package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.VisitClientService
import com.nuzhnov.workcontrol.shared.visitservice.domen.service.nsd.NsdVisitClientService
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class StartVisitClientServiceUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(serviceName: String, clientID: Long) {
        Intent(context, NsdVisitClientService::class.java).apply {
            putExtra(VisitClientService.SERVICE_NAME_EXTRA, serviceName)
            putExtra(VisitClientService.CLIENT_ID_EXTRA, clientID)

            context.startService(this)
        }
    }
}
