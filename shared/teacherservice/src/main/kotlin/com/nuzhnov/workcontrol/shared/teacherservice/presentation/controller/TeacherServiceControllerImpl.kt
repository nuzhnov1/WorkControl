package com.nuzhnov.workcontrol.shared.teacherservice.presentation.controller

import com.nuzhnov.workcontrol.shared.teacherservice.presentation.service.NsdTeacherService
import com.nuzhnov.workcontrol.shared.teacherservice.presentation.resources.toResourceString
import com.nuzhnov.workcontrol.shared.teacherservice.domen.controller.TeacherServiceController
import com.nuzhnov.workcontrol.shared.teacherservice.domen.usecase.internal.UpdateTeacherServiceNameUseCase
import com.nuzhnov.workcontrol.shared.teacherservice.domen.usecase.internal.UpdateLessonIdUseCase
import com.nuzhnov.workcontrol.shared.models.Lesson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import android.content.Context
import android.content.Intent

internal class TeacherServiceControllerImpl @Inject constructor(
    private val updateServiceNameUseCase: UpdateTeacherServiceNameUseCase,
    private val updateLessonIdUseCase: UpdateLessonIdUseCase,
    @ApplicationContext private val context: Context
) : TeacherServiceController {

    override fun startTeacherService(
        lesson: Lesson,
        boundActivityClass: Class<*>,
        notificationChannelID: Long
    ) {
        updateServiceNameUseCase(serviceName = lesson.toServiceName())
        updateLessonIdUseCase(lessonID = lesson.id)

        Intent(context, NsdTeacherService::class.java).apply {
            putExtra(
                /* name = */ NsdTeacherService.CONTENT_ACTIVITY_CLASS_NAME_EXTRA,
                /* value = */ boundActivityClass.canonicalName!!
            )

            putExtra(
                /* name = */ NsdTeacherService.NOTIFICATION_CHANNEL_ID_EXTRA,
                /* value = */ notificationChannelID
            )

            context.startService(this)
        }
    }

    override fun stopTeacherService() {
        context.stopService(Intent(context, NsdTeacherService::class.java))

        updateServiceNameUseCase(serviceName = null)
        updateLessonIdUseCase(lessonID = null)
    }

    private fun Lesson.toServiceName(): String =
        "${discipline.name.replaceFirstChar { char -> char.uppercase() }}. " +
        "${type.toResourceString(context)}. $theme. ${teacher.name}"
}
