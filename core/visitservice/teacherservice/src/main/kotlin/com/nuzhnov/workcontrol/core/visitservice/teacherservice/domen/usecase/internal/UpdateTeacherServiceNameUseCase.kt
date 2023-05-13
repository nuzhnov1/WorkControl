package com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.repository.TeacherServiceRepository
import javax.inject.Inject

internal class UpdateTeacherServiceNameUseCase @Inject constructor(
    private val repository: TeacherServiceRepository
) {
    operator fun invoke(serviceName: String?): Unit =
        repository.updateServiceName(name = serviceName)
}
