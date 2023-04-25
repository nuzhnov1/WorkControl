package com.nuzhnov.workcontrol.core.teacherservice.domen.usecase

import com.nuzhnov.workcontrol.core.teacherservice.domen.controller.TeacherServiceController
import javax.inject.Inject

class StopLessonUseCase @Inject internal constructor(
    private val controller: TeacherServiceController
) {
    operator fun invoke() {
        controller.stopTeacherService()
    }
}
