package com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.controller

import com.nuzhnov.workcontrol.core.models.Lesson

interface TeacherServiceController {
    fun startTeacherService(
        lesson: Lesson,
        boundActivityClass: Class<*>,
        notificationChannelID: Long
    )

    fun stopTeacherService()
}
