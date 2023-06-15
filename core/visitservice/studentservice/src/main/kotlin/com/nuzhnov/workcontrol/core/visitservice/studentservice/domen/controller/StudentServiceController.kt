package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.controller

import com.nuzhnov.workcontrol.core.models.Student
import kotlin.reflect.KClass

internal interface StudentServiceController {
    fun discoverServices(
        boundActivity: KClass<*>,
        notificationChannelID: String
    )

    fun connectToService(
        serviceName: String,
        student: Student,
        boundActivity: KClass<*>,
        notificationChannelID: String
    )

    fun stopStudentService()
}
