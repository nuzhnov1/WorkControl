package com.nuzhnov.workcontrol.shared.teacherservice.domen.usecase

import com.nuzhnov.workcontrol.shared.teacherservice.domen.controller.TeacherServiceController
import com.nuzhnov.workcontrol.shared.models.Lesson
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
