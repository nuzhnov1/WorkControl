package com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.controller

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.service.NsdTeacherService
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.controller.TeacherServiceController
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal.UpdateTeacherServiceNameUseCase
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal.UpdateLessonIdUseCase
import com.nuzhnov.workcontrol.core.model.Lesson
import kotlin.reflect.KClass
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

internal class TeacherServiceControllerImpl @Inject constructor(
    private val updateServiceNameUseCase: UpdateTeacherServiceNameUseCase,
    private val updateLessonIdUseCase: UpdateLessonIdUseCase,
    @ApplicationContext private val context: Context
) : TeacherServiceController {

    override fun startTeacherService(
        lesson: Lesson,
        boundActivityClass: KClass<*>,
        notificationChannelID: Long
    ) {
        updateServiceNameUseCase(serviceName = lesson.toServiceName())
        updateLessonIdUseCase(lessonID = lesson.id)

        Intent(context, NsdTeacherService::class.java).apply {
            putExtra(
                /* name = */ NsdTeacherService.CONTENT_ACTIVITY_CLASS_NAME_EXTRA,
                /* value = */ boundActivityClass.java.canonicalName!!
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
        "${discipline.name.replaceFirstChar { char -> char.uppercase() }}. $theme. ${teacher.name}"
}
