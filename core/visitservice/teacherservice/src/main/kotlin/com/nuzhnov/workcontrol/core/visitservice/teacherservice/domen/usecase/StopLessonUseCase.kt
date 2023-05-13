package com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.controller.TeacherServiceController
import javax.inject.Inject

class StopLessonUseCase @Inject internal constructor(
    private val controller: TeacherServiceController
) {
    operator fun invoke(): Unit = controller.stopTeacherService()
}
