package com.nuzhnov.workcontrol.shared.visitservice.domen.usecase.visitor

import com.nuzhnov.workcontrol.shared.visitservice.domen.service.NsdVisitorService
import com.nuzhnov.workcontrol.shared.visitservice.domen.service.model.VisitorServiceCommand
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

class DiscoverServicesUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(visitorID: Long) {
        Intent(context, NsdVisitorService::class.java).apply {
            putExtra(NsdVisitorService.VISITOR_ID, visitorID)
            putExtra(NsdVisitorService.COMMAND_EXTRA, VisitorServiceCommand.Discover)

            context.startService(this)
        }
    }
}
