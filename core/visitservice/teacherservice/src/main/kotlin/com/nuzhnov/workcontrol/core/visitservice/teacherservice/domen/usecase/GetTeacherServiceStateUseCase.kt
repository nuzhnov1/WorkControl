package com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.repository.TeacherServiceRepository
import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.model.TeacherServiceState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetTeacherServiceStateUseCase @Inject internal constructor(
    private val repository: TeacherServiceRepository
) {
    operator fun invoke(): StateFlow<TeacherServiceState> = repository.serviceState
}
