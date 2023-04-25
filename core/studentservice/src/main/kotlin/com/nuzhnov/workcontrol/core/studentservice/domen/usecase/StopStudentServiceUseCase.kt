package com.nuzhnov.workcontrol.core.studentservice.domen.usecase

import com.nuzhnov.workcontrol.core.studentservice.domen.controller.StudentServiceController
import javax.inject.Inject

class StopStudentServiceUseCase @Inject internal constructor(
    private val controller: StudentServiceController
) {
    operator fun invoke() {
        controller.stopStudentService()
    }
}
