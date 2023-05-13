package com.nuzhnov.workcontrol.core.visitservice.studentservice.presentation.controller

import com.nuzhnov.workcontrol.core.visitservice.studentservice.presentation.service.NsdStudentService
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.controller.StudentServiceController
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase.internal.UpdateStudentIdUseCase
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase.internal.ClearNsdDiscoveredServicesUseCase
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceCommand.*
import com.nuzhnov.workcontrol.core.model.Student
import kotlin.reflect.KClass
import javax.inject.Inject
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext

internal class StudentServiceControllerImpl @Inject constructor(
    private val updateStudentIdUseCase: UpdateStudentIdUseCase,
    private val clearNsdDiscoveredServicesUseCase: ClearNsdDiscoveredServicesUseCase,
    @ApplicationContext private val context: Context
) : StudentServiceController {

    override fun discoverServices(boundActivity: KClass<*>, notificationChannelID: String) {
        clearNsdDiscoveredServicesUseCase()

        Intent(context, NsdStudentService::class.java).apply {
            putExtra(
                /* name = */ NsdStudentService.COMMAND_EXTRA,
                /* value = */ Discover
            )

            putExtra(
                /* name = */ NsdStudentService.CONTENT_ACTIVITY_CLASS_NAME_EXTRA,
                /* value = */ boundActivity.java.canonicalName!!
            )

            putExtra(
                /* name = */ NsdStudentService.NOTIFICATION_CHANNEL_ID_EXTRA,
                /* value = */ notificationChannelID
            )

            context.startService(this)
        }
    }

    override fun connectToService(
        serviceName: String,
        student: Student,
        boundActivity: KClass<*>,
        notificationChannelID: String
    ) {
        updateStudentIdUseCase(studentID = student.id)

        Intent(context, NsdStudentService::class.java).apply {
            putExtra(
                /* name = */ NsdStudentService.COMMAND_EXTRA,
                /* value = */ Connect(serviceName = serviceName)
            )

            putExtra(
                /* name = */ NsdStudentService.CONTENT_ACTIVITY_CLASS_NAME_EXTRA,
                /* value = */ boundActivity.java.canonicalName!!
            )

            putExtra(
                /* name = */ NsdStudentService.NOTIFICATION_CHANNEL_ID_EXTRA,
                /* value = */ notificationChannelID
            )

            context.startService(this)
        }
    }

    override fun stopStudentService() {
        context.stopService(Intent(context, NsdStudentService::class.java))

        clearNsdDiscoveredServicesUseCase()
        updateStudentIdUseCase(studentID = null)
    }
}
