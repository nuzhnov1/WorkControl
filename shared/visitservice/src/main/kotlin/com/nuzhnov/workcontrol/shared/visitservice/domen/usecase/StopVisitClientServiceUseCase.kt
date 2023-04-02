package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.nsd.NsdVisitClientService
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class StopVisitClientServiceUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        Intent(context, NsdVisitClientService::class.java).apply {
            context.stopService(this)
        }
    }
}
