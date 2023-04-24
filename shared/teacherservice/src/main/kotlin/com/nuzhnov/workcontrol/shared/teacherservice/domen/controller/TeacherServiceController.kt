package com.nuzhnov.workcontrol.shared.teacherservice.domen.controller

import com.nuzhnov.workcontrol.shared.models.Lesson

interface TeacherServiceController {
    fun startTeacherService(
        lesson: Lesson,
        boundActivityClass: Class<*>,
        notificationChannelID: Long
    )

    fun stopTeacherService()
}
