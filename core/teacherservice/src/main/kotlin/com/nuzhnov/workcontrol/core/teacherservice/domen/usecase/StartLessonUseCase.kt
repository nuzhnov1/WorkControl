package com.nuzhnov.workcontrol.core.teacherservice.domen.usecase

import com.nuzhnov.workcontrol.core.teacherservice.domen.controller.TeacherServiceController
import com.nuzhnov.workcontrol.core.models.Lesson
import javax.inject.Inject

class StartLessonUseCase @Inject internal constructor(
    private val controller: TeacherServiceController
) {
    operator fun invoke(
        lesson: Lesson,
        boundActivityClass: Class<*>,
        notificationChannelID: Long
    ) {
        controller.startTeacherService(
            lesson = lesson,
            boundActivityClass = boundActivityClass,
            notificationChannelID = notificationChannelID
        )
    }
}
