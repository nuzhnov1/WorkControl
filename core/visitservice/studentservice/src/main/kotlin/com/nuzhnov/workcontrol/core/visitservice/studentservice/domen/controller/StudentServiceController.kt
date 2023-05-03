package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.controller

import com.nuzhnov.workcontrol.core.model.Student

internal interface StudentServiceController {
    fun discoverServices(
        boundActivity: Class<*>,
        notificationChannelID: String
    )

    fun connectToService(
        serviceName: String,
        student: Student,
        boundActivity: Class<*>,
        notificationChannelID: String
    )

    fun stopStudentService()
}
