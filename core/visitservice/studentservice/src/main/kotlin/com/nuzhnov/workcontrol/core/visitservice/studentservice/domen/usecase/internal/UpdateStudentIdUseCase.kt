package com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.visitservice.studentservice.domen.repository.StudentServiceRepository
import javax.inject.Inject

internal class UpdateStudentIdUseCase @Inject constructor(
    private val repository: StudentServiceRepository
) {
    operator fun invoke(studentID: Long?) {
        repository.updateStudentID(id = studentID)
    }
}
