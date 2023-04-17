package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.visitor.NsdVisitorService
import com.nuzhnov.workcontrol.shared.visitservice.domen.service.visitor.VisitorServiceCommand
import com.nuzhnov.workcontrol.core.visitcontrol.model.VisitorID
import javax.inject.Inject
import android.app.Activity
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class ConnectToServiceUseCase @Inject internal constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(
        boundActivity: Class<out Activity>,
        visitorID: VisitorID,
        serviceName: String,
        notificationChannelID: String
    ) {
        Intent(context, NsdVisitorService::class.java).apply {
            putExtra(
                NsdVisitorService.CONTENT_ACTIVITY_CLASS_NAME_EXTRA,
                boundActivity.canonicalName!!
            )
            putExtra(NsdVisitorService.VISITOR_ID_EXTRA, visitorID)
            putExtra(NsdVisitorService.COMMAND_EXTRA, VisitorServiceCommand.Connect(serviceName))
            putExtra(NsdVisitorService.NOTIFICATION_CHANNEL_ID_EXTRA, notificationChannelID)

            context.startService(this)
        }
    }
}
