package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.repository.StudentServiceRepository
import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.model.StudentServiceState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetStudentServiceStateUseCase @Inject internal constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke(): StateFlow<StudentServiceState> = repository.serviceState
}
