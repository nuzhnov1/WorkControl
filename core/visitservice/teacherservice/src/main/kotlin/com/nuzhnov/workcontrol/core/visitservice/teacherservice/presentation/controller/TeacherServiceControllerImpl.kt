package com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.controller

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.service.NsdTeacherService
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.controller.TeacherServiceController
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal.UpdateTeacherServiceNameUseCase
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal.UpdateLessonIdUseCase
import com.nuzhnov.workcontrol.core.models.Lesson
import kotlin.reflect.KClass
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.presentation.resources.generateServiceName
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
        updateServiceNameUseCase(serviceName = generateServiceName())
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

    private fun generateServiceName(): String = context.generateServiceName(
        number = (MIN_SERVICE_ID..MAX_SERVICE_ID).random()
    )


    private companion object {
        const val MIN_SERVICE_ID = 1
        const val MAX_SERVICE_ID = 100
    }
}
