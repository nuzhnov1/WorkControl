package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.nsd.NsdVisitControlService
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class StopVisitControlServiceUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        Intent(context, NsdVisitControlService::class.java).apply {
            context.stopService(this)
        }
    }
}
