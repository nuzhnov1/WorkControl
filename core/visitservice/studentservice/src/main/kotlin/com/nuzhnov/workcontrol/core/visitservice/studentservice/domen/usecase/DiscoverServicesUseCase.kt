package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.controller.StudentServiceController
import javax.inject.Inject

class DiscoverServicesUseCase @Inject internal constructor(
    private val controller: StudentServiceController
) {
    operator fun invoke(boundActivity: Class<*>, notificationChannelID: String): Unit =
        controller.discoverServices(boundActivity, notificationChannelID)
}
