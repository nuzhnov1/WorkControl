package com.nuzhnov.workcontrol.shared.teacherservice.domen.usecase.internal

import com.nuzhnov.workcontrol.shared.teacherservice.domen.repository.TeacherServiceRepository
import javax.inject.Inject

internal class UpdateLessonIdUseCase @Inject constructor(
    private val repository: TeacherServiceRepository
) {
    operator fun invoke(lessonID: Long?) {
        repository.updateLessonID(id = lessonID)
    }
}
