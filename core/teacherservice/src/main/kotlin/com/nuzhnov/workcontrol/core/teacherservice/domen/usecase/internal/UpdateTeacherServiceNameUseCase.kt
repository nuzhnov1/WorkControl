package com.nuzhnov.workcontrol.core.teacherservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.teacherservice.domen.repository.TeacherServiceRepository
import javax.inject.Inject

internal class UpdateTeacherServiceNameUseCase @Inject constructor(
    private val repository: TeacherServiceRepository
) {
    operator fun invoke(serviceName: String?) {
        repository.updateServiceName(name = serviceName)
    }
}
