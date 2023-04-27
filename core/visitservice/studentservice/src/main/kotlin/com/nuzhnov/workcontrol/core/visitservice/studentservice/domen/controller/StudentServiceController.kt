package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.controller

internal interface StudentServiceController {
    fun discoverServices(
        boundActivity: Class<*>,
        notificationChannelID: String
    )

    fun connectToService(
        serviceName: String,
        studentID: Long,
        boundActivity: Class<*>,
        notificationChannelID: String
    )

    fun stopStudentService()
}