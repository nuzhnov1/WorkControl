package com.nuzhnov.workcontrol.shared.studentservice.presentation.controller

import com.nuzhnov.workcontrol.shared.studentservice.presentation.service.NsdStudentService
import com.nuzhnov.workcontrol.shared.studentservice.domen.controller.StudentServiceController
import com.nuzhnov.workcontrol.shared.studentservice.domen.usecase.internal.UpdateStudentIdUseCase
import com.nuzhnov.workcontrol.shared.studentservice.domen.usecase.internal.ClearNsdDiscoveredServicesUseCase
import com.nuzhnov.workcontrol.shared.studentservice.domen.model.StudentServiceCommand.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import android.content.Context
import android.content.Intent

internal class StudentServiceControllerImpl @Inject constructor(
    private val updateStudentIdUseCase: UpdateStudentIdUseCase,
    private val clearNsdDiscoveredServicesUseCase: ClearNsdDiscoveredServicesUseCase,
    @ApplicationContext private val context: Context
) : StudentServiceController {

    override fun discoverServices(boundActivity: Class<*>, notificationChannelID: String) {
        clearNsdDiscoveredServicesUseCase()

        Intent(context, NsdStudentService::class.java).apply {
            putExtra(
                /* name = */ NsdStudentService.COMMAND_EXTRA,
                /* value = */ Discover
            )

            putExtra(
                /* name = */ NsdStudentService.CONTENT_ACTIVITY_CLASS_NAME_EXTRA,
                /* value = */ boundActivity.canonicalName!!
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
        studentID: Long,
        boundActivity: Class<*>,
        notificationChannelID: String
    ) {
        updateStudentIdUseCase(studentID = studentID)

        Intent(context, NsdStudentService::class.java).apply {
            putExtra(
                /* name = */ NsdStudentService.COMMAND_EXTRA,
                /* value = */ Connect(serviceName = serviceName)
            )

            putExtra(
                /* name = */ NsdStudentService.CONTENT_ACTIVITY_CLASS_NAME_EXTRA,
                /* value = */ boundActivity.canonicalName!!
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
