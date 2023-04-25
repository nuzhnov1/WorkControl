package com.nuzhnov.workcontrol.shared.studentservice.domen.usecase

import com.nuzhnov.workcontrol.shared.studentservice.domen.controller.StudentServiceController
import javax.inject.Inject

class StopStudentServiceUseCase @Inject internal constructor(
    private val controller: StudentServiceController
) {
    operator fun invoke() {
        controller.stopStudentService()
    }
}
