package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.NsdVisitorService
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class StopVisitorServiceUseCase @Inject internal constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        context.stopService(Intent(context, NsdVisitorService::class.java))
    }
}
