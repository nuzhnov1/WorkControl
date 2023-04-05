package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.NsdVisitorService
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class StopVisitorServiceUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        Intent(context, NsdVisitorService::class.java).apply {
            context.stopService(this)
        }
    }
}
