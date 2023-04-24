package com.nuzhnov.workcontrol.shared.teacherservice.domen.usecase.internal

import com.nuzhnov.workcontrol.shared.teacherservice.domen.repository.TeacherServiceRepository
import javax.inject.Inject

internal class StartControlUseCase @Inject constructor(
    private val repository: TeacherServiceRepository
) {
    suspend operator fun invoke() {
        repository.startControl()
    }
}
