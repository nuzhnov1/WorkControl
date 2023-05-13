package com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.controller

import com.nuzhnov.workcontrol.core.model.Lesson
import kotlin.reflect.KClass

internal interface TeacherServiceController {
    fun startTeacherService(
        lesson: Lesson,
        boundActivityClass: KClass<*>,
        notificationChannelID: Long
    )

    fun stopTeacherService()
}
