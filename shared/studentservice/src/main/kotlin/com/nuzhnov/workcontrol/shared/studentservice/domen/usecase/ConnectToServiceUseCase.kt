package com.nuzhnov.workcontrol.shared.studentservice.domen.usecase

import com.nuzhnov.workcontrol.shared.studentservice.domen.controller.StudentServiceController
import javax.inject.Inject

class ConnectToServiceUseCase @Inject internal constructor(
    private val controller: StudentServiceController
) {
    operator fun invoke(
        serviceName: String,
        studentID: Long,
        boundActivity: Class<*>,
        notificationChannelID: String
    ) {
        controller.connectToService(
            serviceName = serviceName,
            studentID = studentID,
            boundActivity = boundActivity,
            notificationChannelID = notificationChannelID
        )
    }
}
