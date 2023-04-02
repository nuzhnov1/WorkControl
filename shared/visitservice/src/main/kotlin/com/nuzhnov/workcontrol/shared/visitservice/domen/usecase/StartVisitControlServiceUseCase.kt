package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.VisitControlService
import com.nuzhnov.workcontrol.shared.visitservice.domen.service.nsd.NsdVisitControlService
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class StartVisitControlServiceUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(serviceName: String) {
        Intent(context, NsdVisitControlService::class.java).apply {
            putExtra(VisitControlService.SERVICE_NAME_EXTRA, serviceName)
            context.startService(this)
        }
    }
}
