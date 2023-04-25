package com.nuzhnov.workcontrol.shared.studentservice.domen.usecase

import com.nuzhnov.workcontrol.shared.studentservice.domen.controller.StudentServiceController
import javax.inject.Inject

class DiscoverServicesUseCase @Inject internal constructor(
    private val controller: StudentServiceController
) {
    operator fun invoke(boundActivity: Class<*>, notificationChannelID: String) {
        controller.discoverServices(boundActivity, notificationChannelID)
    }
}
