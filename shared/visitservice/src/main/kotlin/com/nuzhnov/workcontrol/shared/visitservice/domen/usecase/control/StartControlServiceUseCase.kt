package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.control

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.control.NsdControlService
import javax.inject.Inject
import android.app.Activity
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class StartControlServiceUseCase @Inject internal constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(boundActivity: Class<out Activity>, serviceName: String) {
        Intent(context, NsdControlService::class.java).apply {
            putExtra(
                NsdControlService.BOUND_ACTIVITY_CLASS_NAME_EXTRA,
                boundActivity.canonicalName!!
            )
            putExtra(NsdControlService.SERVICE_NAME_EXTRA, serviceName)

            context.startService(this)
        }
    }
}
