package com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.usecase.internal

import com.nuzhnov.workcontrol.core.visitservice.teacherservice.domen.repository.TeacherServiceRepository
import javax.inject.Inject

internal class UpdateLessonIdUseCase @Inject constructor(
    private val repository: TeacherServiceRepository
) {
    operator fun invoke(lessonID: Long?) {
        repository.updateLessonID(id = lessonID)
    }
}
