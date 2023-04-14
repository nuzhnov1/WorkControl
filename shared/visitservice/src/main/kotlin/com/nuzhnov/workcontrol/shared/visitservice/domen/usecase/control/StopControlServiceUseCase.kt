package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.control

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.control.NsdControlService
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class StopControlServiceUseCase @Inject internal constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        context.stopService(Intent(context, NsdControlService::class.java))
    }
}
