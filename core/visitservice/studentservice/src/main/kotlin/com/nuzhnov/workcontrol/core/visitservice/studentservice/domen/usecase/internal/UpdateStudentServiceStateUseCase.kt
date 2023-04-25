package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.repository.StudentServiceRepository
import javax.inject.Inject

internal class UpdateStudentServiceStateUseCase @Inject constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke(serviceState: StudentServiceState) {
        repository.updateServiceState(state = serviceState)
    }
}
