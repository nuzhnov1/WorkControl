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
    operator fun invoke(
        boundActivity: Class<out Activity>,
        serviceName: String,
        notificationChannelID: String
    ) {
        Intent(context, NsdControlService::class.java).apply {
            putExtra(
                NsdControlService.CONTENT_ACTIVITY_CLASS_NAME_EXTRA,
                boundActivity.canonicalName!!
            )
            putExtra(NsdControlService.SERVICE_NAME_EXTRA, serviceName)
            putExtra(NsdControlService.NOTIFICATION_CHANNEL_ID_EXTRA, notificationChannelID)

            context.startService(this)
        }
    }
}
