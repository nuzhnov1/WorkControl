package com.nuzhnov.workcontrol.shared.teacherservice.domen.usecase.internal

import com.nuzhnov.workcontrol.shared.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.shared.teacherservice.domen.repository.TeacherServiceRepository
import javax.inject.Inject

internal class UpdateTeacherServiceStateUseCase @Inject constructor(
    private val repository: TeacherServiceRepository
) {
    operator fun invoke(serviceState: TeacherServiceState) {
        repository.updateServiceState(state = serviceState)
    }
}
