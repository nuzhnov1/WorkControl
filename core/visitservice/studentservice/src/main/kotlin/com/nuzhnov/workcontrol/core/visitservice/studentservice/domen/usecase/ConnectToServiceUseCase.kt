package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.controller.StudentServiceController
import com.nuzhnov.workcontrol.core.models.Student
import kotlin.reflect.KClass
import javax.inject.Inject

class ConnectToServiceUseCase @Inject internal constructor(
    private val controller: StudentServiceController
) {
    operator fun invoke(
        serviceName: String,
        student: Student,
        boundActivity: KClass<*>,
        notificationChannelID: String
    ): Unit = controller.connectToService(
        serviceName = serviceName,
        student = student,
        boundActivity = boundActivity,
        notificationChannelID = notificationChannelID
    )
}
