package com.nuzhnov.workcontrol.shared.teacherservice.domen.usecase

import com.nuzhnov.workcontrol.shared.teacherservice.domen.repository.TeacherServiceRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetTeacherServiceNameUseCase @Inject internal constructor(
    private val repository: TeacherServiceRepository
) {
    operator fun invoke(): StateFlow<String?> = repository.serviceName
}
