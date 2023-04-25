package com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.repository.TeacherServiceRepository
import javax.inject.Inject

internal class StartControlUseCase @Inject constructor(
    private val repository: TeacherServiceRepository
) {
    suspend operator fun invoke() {
        repository.startControl()
    }
}
