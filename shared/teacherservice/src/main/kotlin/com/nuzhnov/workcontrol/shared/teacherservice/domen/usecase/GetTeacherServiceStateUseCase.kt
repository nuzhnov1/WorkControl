package com.nuzhnov.workcontrol.shared.teacherservice.domen.usecase

import com.nuzhnov.workcontrol.shared.teacherservice.domen.model.TeacherServiceState
import com.nuzhnov.workcontrol.shared.teacherservice.domen.repository.TeacherServiceRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetTeacherServiceStateUseCase @Inject internal constructor(
    private val repository: TeacherServiceRepository
) {
    operator fun invoke(): StateFlow<TeacherServiceState> = repository.serviceState
}